
前言
在自定义ViewGroup的过程中，如果涉及到View的拖动滑动，ViewDragHelper的使用应该是少不了的，它提供了一系列用于用户拖动子View的辅助方法和相关的状态记录，像Navigation Drawer的边缘滑动、QQ5.x的侧滑菜单、知乎里的页面滑动返回都可以由它实现，所以有必要完全掌握它的使用。

要想完全掌握ViewDragHelper的使用和原理，最好的办法就是读懂它的源码，所以就有了这篇分析，以便在印象模糊之时可以再次快速回顾ViewDragHelper的原理、用法、注意事项等。

基本用法
在自定义ViewGroup的构造方法里调用ViewDragHelper的静态工厂方法create()创建ViewDragHelper实例
实现ViewDragHelper.Callback
最重要的几个方法是tryCaptureView()、clampViewPositionVertical()、clampViewPositionHorizontal()、getViewHorizontalDragRange()、getViewVerticalDragRange()

tryCaptureView()里会传递当前触摸区域下的子View实例作为参数，如果需要对当前触摸的子View进行拖拽移动就返回true，否则返回false。
clampViewPositionVertical()决定了要拖拽的子View在垂直方向上应该移动到的位置，该方法会传递三个参数：要拖拽的子View实例、期望的移动后位置子View的top值、移动的距离。返回值为子View在最终位置时的top值，一般直接返回第二个参数即可。
clampViewPositionHorizontal()与clampViewPositionVertical()同理，只不过是发生在水平方向上，最终返回的是View的left值。
getViewVerticalDragRange()要返回一个大于0的数，才会在在垂直方向上对触摸到的View进行拖动。
getViewHorizontalDragRange()与getViewVerticalDragRange()同理，只不过是发生在水平方向上。
在onInterceptTouchEvent()方法里调用并返回ViewDragHelper的shouldInterceptTouchEvent()方法
在onTouchEvent()方法里调用ViewDragHelper()的processTouchEvent()方法。ACTION_DOWN事件发生时，如果当前触摸点下要拖动的子View没有消费事件，此时应该在onTouchEvent()返回true，否则将收不到后续事件，不会产生拖动。
上面几个步骤已经实现了子View拖动的效果，如果还想要实现fling效果（滑动时松手后以一定速率继续自动滑动下去并逐渐停止，类似于扔东西）或者松手后自动滑动到指定位置，需要实现自定义ViewGroup的computeScroll()方法，方法实现如下：

@Override
public void computeScroll() {
	if (mDragHelper.continueSettling(true)) {
		postInvalidate();
	}
}
并在ViewDragHelper.Callback的onViewReleased()方法里调用settleCapturedViewAt()、flingCapturedView()，或在任意地方调用smoothSlideViewTo()方法。
如果要实现边缘拖动的效果，需要调用ViewDragHelper的setEdgeTrackingEnabled()方法，注册想要监听的边缘。然后实现ViewDragHelper.Callback里的onEdgeDragStarted()方法，在此手动调用captureChildView()传递要拖动的子View。

源码详解
ViewDragHelper的完整源码可在GitHub或GrepCode上在线查看

预备知识

了解View的坐标系统，Android View坐标getLeft, getRight, getTop, getBottom
了解MotionEvent中关于多点触控的机制，android触控,先了解MotionEvent(一)
了解Scroller类原理，Android中滑屏实现----手把手教你如何实现触摸滑屏以及Scroller类详解
了解Touch事件的分发机制，Andriod 从源码的角度详解View,ViewGroup的Touch事件的分发机制
ViewDragHelper实例的创建

ViewDragHelper重载了两个create()静态方法，先看两个参数的create()方法：

/**
 * Factory method to create a new ViewDragHelper.
 *
 * @param forParent Parent view to monitor
 * @param cb Callback to provide information and receive events
 * @return a new ViewDragHelper instance
 */
public static ViewDragHelper create(ViewGroup forParent, Callback cb) {
	return new ViewDragHelper(forParent.getContext(), forParent, cb);
}
create()的两个参数很好理解，第一个是我们自定义的ViewGroup，第二个是控制子View拖拽需要的回调对象。create()直接调用了ViewDragHelper构造方法，我们再来看看这个构造方法。

/**
 * Apps should use ViewDragHelper.create() to get a new instance.
 * This will allow VDH to use internal compatibility implementations for different
 * platform versions.
 *
 * @param context Context to initialize config-dependent params from
 * @param forParent Parent view to monitor
 */
private ViewDragHelper(Context context, ViewGroup forParent, Callback cb) {
	if (forParent == null) {
		throw new IllegalArgumentException("Parent view may not be null");
	}
	if (cb == null) {
		throw new IllegalArgumentException("Callback may not be null");
	}

	mParentView = forParent;
	mCallback = cb;

	final ViewConfiguration vc = ViewConfiguration.get(context);
	final float density = context.getResources().getDisplayMetrics().density;
	mEdgeSize = (int) (EDGE_SIZE * density + 0.5f);

	mTouchSlop = vc.getScaledTouchSlop();
	mMaxVelocity = vc.getScaledMaximumFlingVelocity();
	mMinVelocity = vc.getScaledMinimumFlingVelocity();
	mScroller = ScrollerCompat.create(context, sInterpolator);
}
这个构造函数是私有的，也是仅有的构造函数，所以外部只能通过create()工厂方法来创建ViewDragHelper实例了。这里要求了我们传递的自定义ViewGroup和回调对象不能为空，否则会直接抛出异常中断程序。在这里也初始化了一些触摸滑动需要的参考值和辅助类。

mParentView和mCallback分别保存传递过来的对应参数
ViewConfiguration类里定义了View相关的一系列时间、大小、距离等常量
mEdgeSize表示边缘触摸的范围。例如mEdgeSize为20dp并且用户注册监听了左侧边缘触摸时，触摸点的x坐标小于mParentView.getLeft() + mEdgeSize时（即触摸点在容器左边界往右20dp内）就算做是左侧的边缘触摸，详见ViewDragHelper的getEdgesTouched()方法。
mTouchSlop是一个很小的距离值，只有在前后两次触摸点的距离超过mTouchSlop的值时，我们才把这两次触摸算作是“滑动”，我们只在此时进行滑动处理，否则任何微小的距离的变化我们都要处理的话会显得太频繁，如果处理过程又比较复杂耗时就会使界面产生卡顿。
mMaxVelocity、mMinVelocity是fling时的最大、最小速率，单位是像素每秒。
mScroller是View滚动的辅助类，该类的详细解析参见下面几篇文章

Android中滑屏实现----手把手教你如何实现触摸滑屏以及Scroller类详解
Android中Scroller类的分析
再看三个参数的create()方法：

/**
 * Factory method to create a new ViewDragHelper.
 *
 * @param forParent Parent view to monitor
 * @param sensitivity Multiplier for how sensitive the helper should be about detecting
 *                    the start of a drag. Larger values are more sensitive. 1.0f is normal.
 * @param cb Callback to provide information and receive events
 * @return a new ViewDragHelper instance
 */
public static ViewDragHelper create(ViewGroup forParent, float sensitivity, Callback cb) {
	final ViewDragHelper helper = create(forParent, cb);
	helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));
	return helper;
}
第二个参数sensitivity是用来调节mTouchSlop的值。sensitivity越大，mTouchSlop越小，对滑动的检测就越敏感。例如sensitivity为1时，前后触摸点距离超过20dp才进行滑动处理，现在sensitivity为2的话，前后触摸点距离超过10dp就进行处理了。

对Touch事件的处理

当mParentView（自定义ViewGroup）被触摸时，首先会调用mParentView的onInterceptTouchEvent(MotionEvent ev)，接着就调用shouldInterceptTouchEvent(MotionEvent ev) ，所以先来看看这个方法的ACTION_DOWN部分：

/**
 * Check if this event as provided to the parent view's onInterceptTouchEvent should
 * cause the parent to intercept the touch event stream.
 *
 * @param ev MotionEvent provided to onInterceptTouchEvent
 * @return true if the parent view should return true from onInterceptTouchEvent
 */
public boolean shouldInterceptTouchEvent(MotionEvent ev) {
    final int action = MotionEventCompat.getActionMasked(ev);
    final int actionIndex = MotionEventCompat.getActionIndex(ev);

    if (action == MotionEvent.ACTION_DOWN) {
        // Reset things for a new event stream, just in case we didn't get
        // the whole previous stream.
        cancel();
    }

    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    switch (action) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();
            final int pointerId = MotionEventCompat.getPointerId(ev, 0);
            saveInitialMotion(x, y, pointerId);

            final View toCapture = findTopChildUnder((int) x, (int) y);

            // Catch a settling view if possible.
            if (toCapture == mCapturedView && mDragState == STATE_SETTLING) {
                tryCaptureViewForDrag(toCapture, pointerId);
            }

            final int edgesTouched = mInitialEdgesTouched[pointerId];
            if ((edgesTouched & mTrackingEdges) != 0) {
                mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
            }
            break;
        }

		// 其他case暂且省略
    }

    return mDragState == STATE_DRAGGING;
}
看9~21行，首先是关于多点触控（MotionEvent的actionIndex、ACTION_POINTER_DOWN等概念），不明白的请参阅android触控,先了解MotionEvent(一)。

mVelocityTracker记录下触摸的各个点信息，稍后可以用来计算本次滑动的速率，每次发生ACTION_DOWN事件都会调用cancel()，而在cancel()方法里mVelocityTracker又被清空了，所以mVelocityTracker记录下的是本次ACTION_DOWN事件直至ACTION_UP事件发生后（下次ACTION_DOWN事件发生前）的所有触摸点的信息。

再来看24~42行case MotionEvent.ACTION_DOWN部分，先是调用saveInitialMotion(x, y, pointerId)保存手势的初始信息，即ACTION_DOWN发生时的触摸点坐标（x、y）、触摸手指编号（pointerId），如果触摸到了mParentView的边缘还会记录触摸的是哪个边缘。接着调用findTopChildUnder((int) x, (int) y);来获取当前触摸点下最顶层的子View，看findTopChildUnder的源码：

/**
 * Find the topmost child under the given point within the parent view's coordinate system.
 * The child order is determined using {@link Callback#getOrderedChildIndex(int)}.
 *
 * @param x X position to test in the parent's coordinate system
 * @param y Y position to test in the parent's coordinate system
 * @return The topmost child view under (x, y) or null if none found.
 */
public View findTopChildUnder(int x, int y) {
    final int childCount = mParentView.getChildCount();
    for (int i = childCount - 1; i >= 0; i--) {
        final View child = mParentView.getChildAt(mCallback.getOrderedChildIndex(i));
        if (x >= child.getLeft() && x < child.getRight() &&
                y >= child.getTop() && y < child.getBottom()) {
            return child;
        }
    }
    return null;
}
代码很简单，注释里也说明的很清楚了。如果在同一个位置有两个子View重叠，想要让下层的子View被选中，那么就要实现Callback里的getOrderedChildIndex(int index)方法来改变查找子View的顺序；例如topView（上层View）的index是4，bottomView（下层View）的index是3，按照正常的遍历查找方式（getOrderedChildIndex()默认直接返回index），会选择到topView，要想让bottomView被选中就得这么写：

public int getOrderedChildIndex(int index) {
	int indexTop = mParentView.indexOfChild(topView);
	int indexBottom = mParentView.indexOfChild(bottomView);
	if (index == indexTop) {
		return indexBottom;
	}
	return index;
}
32~35行，这里还看到了一个mDragState成员变量，它共有三种取值：

STATE_IDLE：所有的View处于静止空闲状态
STATE_DRAGGING：某个View正在被用户拖动（用户正在与设备交互）
STATE_SETTLING：某个View正在安置状态中（用户并没有交互操作），就是自动滚动的过程中
mCapturedView默认为null，所以一开始不会执行这里的代码，mDragState处于STATE_SETTLING状态时才会执行tryCaptureViewForDrag()，执行的情况到后面再分析，这里先跳过。
37~40行调用了Callback.onEdgeTouched向外部通知mParentView的某些边缘被触摸到了，mInitialEdgesTouched是在刚才调用过的saveInitialMotion方法里进行赋值的。

ACTION_DOWN部分处理完了，跳过switch语句块，剩下的代码就只有return mDragState == STATE_DRAGGING;。在ACTION_DOWN部分没有对mDragState进行赋值，其默认值为STATE_IDLE，所以此处返回false。

那么返回false后接下来应该是会调用哪个方法呢，根据Andriod 从源码的角度详解View,ViewGroup的Touch事件的分发机制里的解析，接下来会在mParentView的所有子View中寻找响应这个Touch事件的View（会调用每个子View的dispatchTouchEvent()方法，dispatchTouchEvent里一般又会调用onTouchEvent()）；

如果没有子View消费这次事件（子View的dispatchTouchEvent()返回都是false），会调用mParentView的super.dispatchTouchEvent(ev)，即View中的dispatchTouchEvent(ev)，然后调用mParentView的onTouchEvent()方法，再调用ViewDragHelper的processTouchEvent(MotionEvent ev)方法。此时（ACTION_DOWN事件发生时）mParentView的onTouchEvent()要返回true，onTouchEvent()才能继续接受到接下来的ACTION_MOVE、ACTION_UP等事件，否则无法完成拖动（除了ACTION_DOWN外的其他事件发生时返回true或false都不会影响接下来的事件接受），因为拖动的相关代码是写在processTouchEvent()里的ACTION_MOVE部分的。要注意的是返回true后mParentView的onInterceptTouchEvent()就不会收到后续的ACTION_MOVE、ACTION_UP等事件了。

如果有子View消费了本次ACTION_DOWN事件，mParentView的onTouchEvent()就收不到ACTION_DOWN事件了，也就是ViewDragHelper的processTouchEvent(MotionEvent ev)收不到ACTION_DOWN事件了。不过只要该View没有调用过requestDisallowInterceptTouchEvent(true)，mParentView的onInterceptTouchEvent()的ACTION_MOVE部分还是会执行的，如果在此时返回了true拦截了ACTION_MOVE事件，processTouchEvent()里的ACTION_MOVE部分也就会正常执行，拖动也就没问题了。onInterceptTouchEvent()的ACTION_MOVE部分具体做了怎样的处理，稍后再来解析。

接下来对这两种情况逐一解析。

假设没有子View消费这次事件，根据刚才的分析最终就会调用processTouchEvent(MotionEvent ev)的ACTION_DOWN部分：

/**
 * Process a touch event received by the parent view. This method will dispatch callback events
 * as needed before returning. The parent view's onTouchEvent implementation should call this.
 *
 * @param ev The touch event received by the parent view
 */
public void processTouchEvent(MotionEvent ev) {
    final int action = MotionEventCompat.getActionMasked(ev);
    final int actionIndex = MotionEventCompat.getActionIndex(ev);

    if (action == MotionEvent.ACTION_DOWN) {
        // Reset things for a new event stream, just in case we didn't get
        // the whole previous stream.
        cancel();
    }

    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(ev);

    switch (action) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();
            final int pointerId = MotionEventCompat.getPointerId(ev, 0);
            final View toCapture = findTopChildUnder((int) x, (int) y);

            saveInitialMotion(x, y, pointerId);

            // Since the parent is already directly processing this touch event,
            // there is no reason to delay for a slop before dragging.
            // Start immediately if possible.
            tryCaptureViewForDrag(toCapture, pointerId);

            final int edgesTouched = mInitialEdgesTouched[pointerId];
            if ((edgesTouched & mTrackingEdges) != 0) {
                mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
            }
            break;
        }
		// 其他case暂且省略
    }
}
这段代码跟shouldInterceptTouchEvent()里ACTION_DOWN那部分基本一致，唯一区别就是这里没有约束条件直接调用了tryCaptureViewForDrag()方法，现在来看看这个方法：

/**
 * Attempt to capture the view with the given pointer ID. The callback will be involved.
 * This will put us into the "dragging" state. If we've already captured this view with
 * this pointer this method will immediately return true without consulting the callback.
 *
 * @param toCapture View to capture
 * @param pointerId Pointer to capture with
 * @return true if capture was successful
 */
boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
    if (toCapture == mCapturedView && mActivePointerId == pointerId) {
        // Already done!
        return true;
    }
    if (toCapture != null && mCallback.tryCaptureView(toCapture, pointerId)) {
        mActivePointerId = pointerId;
        captureChildView(toCapture, pointerId);
        return true;
    }
    return false;
}
这里调用了Callback的tryCaptureView(View child, int pointerId)方法，把当前触摸到的View和触摸手指编号传递了过去，在tryCaptureView()中决定是否需要拖动当前触摸到的View，如果要拖动当前触摸到的View就在tryCaptureView()中返回true，让ViewDragHelper把当前触摸的View捕获下来，接着就调用了captureChildView(toCapture, pointerId)方法：

/**
 * Capture a specific child view for dragging within the parent. The callback will be notified
 * but {@link Callback#tryCaptureView(android.view.View, int)} will not be asked permission to
 * capture this view.
 *
 * @param childView Child view to capture
 * @param activePointerId ID of the pointer that is dragging the captured child view
 */
public void captureChildView(View childView, int activePointerId) {
    if (childView.getParent() != mParentView) {
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant " +
                "of the ViewDragHelper's tracked parent view (" + mParentView + ")");
    }

    mCapturedView = childView;
    mActivePointerId = activePointerId;
    mCallback.onViewCaptured(childView, activePointerId);
    setDragState(STATE_DRAGGING);
}
代码很简单，在captureChildView(toCapture, pointerId)中将要拖动的View和触摸的手指编号记录下来，并调用Callback的onViewCaptured(childView, activePointerId)通知外部有子View被捕获到了，再调用setDragState()设置当前的状态为STATE_DRAGGING，看setDragState()源码：

void setDragState(int state) {
    if (mDragState != state) {
        mDragState = state;
        mCallback.onViewDragStateChanged(state);
        if (mDragState == STATE_IDLE) {
            mCapturedView = null;
        }
    }
}
状态改变后会调用Callback的onViewDragStateChanged()通知状态的变化。

假设ACTION_DOWN发生后在mParentView的onTouchEvent()返回了true，接下来就会执行ACTION_MOVE部分：

public void processTouchEvent(MotionEvent ev) {

    switch (action) {
        // 省略其他case...
		
        case MotionEvent.ACTION_MOVE: {
            if (mDragState == STATE_DRAGGING) {
                final int index = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, index);
                final float y = MotionEventCompat.getY(ev, index);
                final int idx = (int) (x - mLastMotionX[mActivePointerId]);
                final int idy = (int) (y - mLastMotionY[mActivePointerId]);

                dragTo(mCapturedView.getLeft() + idx, mCapturedView.getTop() + idy, idx, idy);

                saveLastMotion(ev);
            } else {
                // Check to see if any pointer is now over a draggable view.
                final int pointerCount = MotionEventCompat.getPointerCount(ev);
                for (int i = 0; i < pointerCount; i++) {
                    final int pointerId = MotionEventCompat.getPointerId(ev, i);
                    final float x = MotionEventCompat.getX(ev, i);
                    final float y = MotionEventCompat.getY(ev, i);
                    final float dx = x - mInitialMotionX[pointerId];
                    final float dy = y - mInitialMotionY[pointerId];

                    reportNewEdgeDrags(dx, dy, pointerId);
                    if (mDragState == STATE_DRAGGING) {
                        // Callback might have started an edge drag.
                        break;
                    }

                    final View toCapture = findTopChildUnder((int) x, (int) y);
                    if (checkTouchSlop(toCapture, dx, dy) &&
                            tryCaptureViewForDrag(toCapture, pointerId)) {
                        break;
                    }
                }
                saveLastMotion(ev);
            }
            break;
        }

		// 省略其他case...
    }
}
要注意的是，如果一直没松手，这部分代码会一直调用。这里先判断mDragState是否为STATE_DRAGGING，而唯一调用setDragState(STATE_DRAGGING)的地方就是tryCaptureViewForDrag()了，刚才在ACTION_DOWN里调用过tryCaptureViewForDrag()，现在又要分两种情况。
如果刚才在ACTION_DOWN里捕获到要拖动的View，那么就执行if部分的代码，这个稍后解析，先考虑没有捕获到的情况。没有捕获到的话，mDragState依然是STATE_IDLE，然后会执行else部分的代码。这里主要就是检查有没有哪个手指触摸到了要拖动的View上，触摸上了就尝试捕获它，然后让mDragState变为STATE_DRAGGING，之后就会执行if部分的代码了。这里还有两个方法涉及到了Callback里的方法，需要来解析一下，分别是reportNewEdgeDrags()和checkTouchSlop()，先看reportNewEdgeDrags()：

private void reportNewEdgeDrags(float dx, float dy, int pointerId) {
    int dragsStarted = 0;
    if (checkNewEdgeDrag(dx, dy, pointerId, EDGE_LEFT)) {
        dragsStarted |= EDGE_LEFT;
    }
    if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_TOP)) {
        dragsStarted |= EDGE_TOP;
    }
    if (checkNewEdgeDrag(dx, dy, pointerId, EDGE_RIGHT)) {
        dragsStarted |= EDGE_RIGHT;
    }
    if (checkNewEdgeDrag(dy, dx, pointerId, EDGE_BOTTOM)) {
        dragsStarted |= EDGE_BOTTOM;
    }

    if (dragsStarted != 0) {
        mEdgeDragsInProgress[pointerId] |= dragsStarted;
        mCallback.onEdgeDragStarted(dragsStarted, pointerId);
    }
}
这里对四个边缘都做了一次检查，检查是否在某些边缘产生拖动了，如果有拖动，就将有拖动的边缘记录在mEdgeDragsInProgress中，再调用Callback的onEdgeDragStarted(int edgeFlags, int pointerId)通知某个边缘开始产生拖动了。虽然reportNewEdgeDrags()会被调用很多次（因为processTouchEvent()的ACTION_MOVE部分会执行很多次），但mCallback.onEdgeDragStarted(dragsStarted, pointerId)只会调用一次，具体的要看checkNewEdgeDrag()这个方法：

private boolean checkNewEdgeDrag(float delta, float odelta, int pointerId, int edge) {
    final float absDelta = Math.abs(delta);
    final float absODelta = Math.abs(odelta);

    if ((mInitialEdgesTouched[pointerId] & edge) != edge  || (mTrackingEdges & edge) == 0 ||
            (mEdgeDragsLocked[pointerId] & edge) == edge ||
            (mEdgeDragsInProgress[pointerId] & edge) == edge ||
            (absDelta <= mTouchSlop && absODelta <= mTouchSlop)) {
        return false;
    }
    if (absDelta < absODelta * 0.5f && mCallback.onEdgeLock(edge)) {
        mEdgeDragsLocked[pointerId] |= edge;
        return false;
    }
    return (mEdgeDragsInProgress[pointerId] & edge) == 0 && absDelta > mTouchSlop;
}
checkNewEdgeDrag()返回true表示在指定的edge（边缘）开始产生拖动了。
方法的两个参数delta和odelta需要解释一下，odelta里的o应该代表opposite，这是什么意思呢，以reportNewEdgeDrags()里调用checkNewEdgeDrag(dx, dy, pointerId, EDGE_LEFT)为例，我们要监测左边缘的触摸情况，所以主要监测的是x轴方向上的变化，这里delta为dx，odelta为dy，也就是说delta是指我们主要监测的方向上的变化，odelta是另外一个方向上的变化，后面要判断假另外一个方向上的变化是否要远大于主要方向上的变化，所以需要另外一个方向上的距离变化的值。
mInitialEdgesTouched是在ACTION_DOWN部分的saveInitialMotion()里生成的，ACTION_DOWN发生时触摸到的边缘会被记录在mInitialEdgesTouched中。如果ACTION_DOWN发生时没有触摸到边缘，或者触摸到的边缘不是指定的edge，就直接返回false了。
mTrackingEdges是由setEdgeTrackingEnabled(int edgeFlags)设置的，当我们想要追踪监听边缘触摸时才需要调用setEdgeTrackingEnabled(int edgeFlags)，如果我们没有调用过它，这里就直接返回false了。
mEdgeDragsLocked它在这个方法里被引用了多次，它在整个ViewDragHelper里唯一被赋值的地方就是这里的第12行，所以默认值是0，第6行mEdgeDragsLocked[pointerId] & edge) == edge执行的结果是false。我们再跳到11到14行看看，absDelta < absODelta * 0.5f的意思是检查在次要方向上移动的距离是否远超过主要方向上移动的距离，如果是再调用Callback的onEdgeLock(edge)检查是否需要锁定某个边缘，如果锁定了某个边缘，那个边缘就算触摸到了也不会被记录在mEdgeDragsInProgress里了，也不会收到Callback的onEdgeDragStarted()通知了。并且将锁定的边缘记录在mEdgeDragsLocked变量里，再次调用本方法时就会在第6行进行判断了，第6行里如果检测到给定的edge被锁定，就直接返回false了。
回到第7行的(mEdgeDragsInProgress[pointerId] & edge) == edge，mEdgeDragsInProgress是保存已发生过拖动事件的边缘的，如果给定的edge已经保存过了，那就没必要再检测其他东西了，直接返回false了。
第8行(absDelta <= mTouchSlop && absODelta <= mTouchSlop)很简单了，就是检查本次移动的距离是不是太小了，太小就不处理了。
最后一句返回的时候再次检查给定的edge有没有记录过，确保了每个边缘只会调用一次reportNewEdgeDrags的mCallback.onEdgeDragStarted(dragsStarted, pointerId)
再来看checkTouchSlop()方法：

/**
 * Check if we've crossed a reasonable touch slop for the given child view.
 * If the child cannot be dragged along the horizontal or vertical axis, motion
 * along that axis will not count toward the slop check.
 *
 * @param child Child to check
 * @param dx Motion since initial position along X axis
 * @param dy Motion since initial position along Y axis
 * @return true if the touch slop has been crossed
 */
private boolean checkTouchSlop(View child, float dx, float dy) {
    if (child == null) {
        return false;
    }
    final boolean checkHorizontal = mCallback.getViewHorizontalDragRange(child) > 0;
    final boolean checkVertical = mCallback.getViewVerticalDragRange(child) > 0;

    if (checkHorizontal && checkVertical) {
        return dx * dx + dy * dy > mTouchSlop * mTouchSlop;
    } else if (checkHorizontal) {
        return Math.abs(dx) > mTouchSlop;
    } else if (checkVertical) {
        return Math.abs(dy) > mTouchSlop;
    }
    return false;
}
这个方法主要就是检查手指移动的距离有没有超过触发处理移动事件的最短距离（mTouchSlop）了，注意dx和dy指的是当前触摸点到ACTION_DOWN触摸到的点的距离。这里先检查Callback的getViewHorizontalDragRange(child)和getViewVerticalDragRange(child)是否大于0，如果想让某个View在某个方向上滑动，就要在那个方向对应的方法里返回大于0的数。否则在processTouchEvent()的ACTION_MOVE部分就不会调用tryCaptureViewForDrag()来捕获当前触摸到的View了，拖动也就没办法进行了。

回到processTouchEvent()的ACTION_MOVE部分，假设现在我们的手指已经滑动到可以被捕获到的View上了，也都正常的实现了Callback中的相关方法，让tryCaptureViewForDrag()正常的捕获到触摸到的View了，下一次ACTION_MOVE时就执行if部分的代码了，也就是开始不停的调用dragTo()对mCaptureView进行真正拖动了，看dragTo()方法：

private void dragTo(int left, int top, int dx, int dy) {
    int clampedX = left;
    int clampedY = top;
    final int oldLeft = mCapturedView.getLeft();
    final int oldTop = mCapturedView.getTop();
    if (dx != 0) {
        clampedX = mCallback.clampViewPositionHorizontal(mCapturedView, left, dx);
        mCapturedView.offsetLeftAndRight(clampedX - oldLeft);
    }
    if (dy != 0) {
        clampedY = mCallback.clampViewPositionVertical(mCapturedView, top, dy);
        mCapturedView.offsetTopAndBottom(clampedY - oldTop);
    }

    if (dx != 0 || dy != 0) {
        final int clampedDx = clampedX - oldLeft;
        final int clampedDy = clampedY - oldTop;
        mCallback.onViewPositionChanged(mCapturedView, clampedX, clampedY,
                clampedDx, clampedDy);
    }
}
参数dx和dy是前后两次ACTION_MOVE移动的距离，left和top分别为mCapturedView.getLeft() + dx, mCapturedView.getTop() + dy，也就是期望的移动后的坐标，对View的getLeft()等方法不理解的请参阅Android View坐标getLeft, getRight, getTop, getBottom。

这里通过调用offsetLeftAndRight()和offsetTopAndBottom()来完成对mCapturedView移动，这两个是View中定义的方法，看它们的源码就知道内部是通过改变View的mLeft、mRight、mTop、mBottom，即改变View在父容器中的坐标位置，达到移动View的效果，所以如果调用mCapturedView的layout(int l, int t, int r, int b)方法也可以实现移动View的效果。

具体要移动到哪里，由Callback的clampViewPositionHorizontal()和clampViewPositionVertical()来决定的，如果不想在水平方向上移动，在clampViewPositionHorizontal(View child, int left, int dx)里直接返回child.getLeft()就可以了，这样clampedX - oldLeft的值为0，这里调用mCapturedView.offsetLeftAndRight(clampedX - oldLeft)就不会起作用了。垂直方向上同理。

最后会调用Callback的onViewPositionChanged(mCapturedView, clampedX, clampedY,clampedDx, clampedDy)通知捕获到的View位置改变了，并把最终的坐标（clampedX、clampedY）和最终的移动距离（clampedDx、 clampedDy）传递过去。

ACTION_MOVE部分就算告一段落了，接下来应该是用户松手触发ACTION_UP，或者是达到某个条件导致后续的ACTION_MOVE被mParentView的上层View给拦截了而收到ACTION_CANCEL，一起来看这两个部分：

public void processTouchEvent(MotionEvent ev) {
    // 省略

    switch (action) {
        // 省略其他case

        case MotionEvent.ACTION_UP: {
            if (mDragState == STATE_DRAGGING) {
                releaseViewForPointerUp();
            }
            cancel();
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            if (mDragState == STATE_DRAGGING) {
                dispatchViewReleased(0, 0);
            }
            cancel();
            break;
        }
    }
}
这两个部分都是重置所有的状态记录，并通知View被放开了，再看下releaseViewForPointerUp()和dispatchViewReleased()的源码：

private void releaseViewForPointerUp() {
    mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
    final float xvel = clampMag(
            VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId),
            mMinVelocity, mMaxVelocity);
    final float yvel = clampMag(
            VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId),
            mMinVelocity, mMaxVelocity);
    dispatchViewReleased(xvel, yvel);
}
releaseViewForPointerUp()里也调用了dispatchViewReleased()，只不过传递了速率给它，这个速率就是由processTouchEvent()的mVelocityTracker追踪算出来的。再看dispatchViewReleased()：

/**
 * Like all callback events this must happen on the UI thread, but release
 * involves some extra semantics. During a release (mReleaseInProgress)
 * is the only time it is valid to call {@link #settleCapturedViewAt(int, int)}
 * or {@link #flingCapturedView(int, int, int, int)}.
 */
private void dispatchViewReleased(float xvel, float yvel) {
    mReleaseInProgress = true;
    mCallback.onViewReleased(mCapturedView, xvel, yvel);
    mReleaseInProgress = false;

    if (mDragState == STATE_DRAGGING) {
        // onViewReleased didn't call a method that would have changed this. Go idle.
        setDragState(STATE_IDLE);
    }
}
这里调用Callback的onViewReleased(mCapturedView, xvel, yvel)通知外部捕获到的View被释放了，而在onViewReleased()前后有个mReleaseInProgress值得注意，注释里说唯一可以调用ViewDragHelper的settleCapturedViewAt()和flingCapturedView()的地方就是在Callback的onViewReleased()里了。

首先这两个方法是干什么的呢。在现实生活中保龄球的打法是，先做扔的动作让球的速度达到最大，然后突然松手，由于惯性，保龄球就以最后松手前的速度为初速度抛出去了，直至自然停止，或者撞到边界停止，这种效果叫fling。
flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop)就是对捕获到的View做出这种fling的效果，用户在屏幕上滑动松手之前也会有一个滑动的速率。fling也引出来的一个问题，就是不知道View最终会滚动到哪个位置，最后位置是在启动fling时根据最后滑动的速度来计算的（flingCapturedView的四个参数int minLeft, int minTop, int maxLeft, int maxTop可以限定最终位置的范围），假如想要让View滚动到指定位置应该怎么办，答案就是使用settleCapturedViewAt(int finalLeft, int finalTop)。

为什么唯一可以调用settleCapturedViewAt()和flingCapturedView()的地方是Callback的onViewReleased()呢？看看它们的源码

/**
 * Settle the captured view at the given (left, top) position.
 * The appropriate velocity from prior motion will be taken into account.
 * If this method returns true, the caller should invoke {@link #continueSettling(boolean)}
 * on each subsequent frame to continue the motion until it returns false. If this method
 * returns false there is no further work to do to complete the movement.
 *
 * @param finalLeft Settled left edge position for the captured view
 * @param finalTop Settled top edge position for the captured view
 * @return true if animation should continue through {@link #continueSettling(boolean)} calls
 */
public boolean settleCapturedViewAt(int finalLeft, int finalTop) {
    if (!mReleaseInProgress) {
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to " +
                "Callback#onViewReleased");
    }

    return forceSettleCapturedViewAt(finalLeft, finalTop,
            (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId),
            (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId));
}

/**
 * Settle the captured view based on standard free-moving fling behavior.
 * The caller should invoke {@link #continueSettling(boolean)} on each subsequent frame
 * to continue the motion until it returns false.
 *
 * @param minLeft Minimum X position for the view's left edge
 * @param minTop Minimum Y position for the view's top edge
 * @param maxLeft Maximum X position for the view's left edge
 * @param maxTop Maximum Y position for the view's top edge
 */
public void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop) {
    if (!mReleaseInProgress) {
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to " +
                "Callback#onViewReleased");
    }

    mScroller.fling(mCapturedView.getLeft(), mCapturedView.getTop(),
            (int) VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId),
            (int) VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId),
            minLeft, maxLeft, minTop, maxTop);

    setDragState(STATE_SETTLING);
}
这两个方法里一开始都会判断mReleaseInProgress为false，如果为false就会抛一个IllegalStateException异常，而mReleaseInProgress唯一为true的时候就是在dispatchViewReleased()里调用onViewReleased()的时候。

Scroller的用法请参阅Android中滑屏实现----手把手教你如何实现触摸滑屏以及Scroller类详解 ，或者自行解读Scroller源码，代码量不多。

ViewDragHelper还有一个移动View的方法是smoothSlideViewTo(View child, int finalLeft, int finalTop)，看下它的源码：

/**
 * Animate the view <code>child</code> to the given (left, top) position.
 * If this method returns true, the caller should invoke {@link #continueSettling(boolean)}
 * on each subsequent frame to continue the motion until it returns false. If this method
 * returns false there is no further work to do to complete the movement.
 *
 * <p>This operation does not count as a capture event, though {@link #getCapturedView()}
 * will still report the sliding view while the slide is in progress.</p>
 *
 * @param child Child view to capture and animate
 * @param finalLeft Final left position of child
 * @param finalTop Final top position of child
 * @return true if animation should continue through {@link #continueSettling(boolean)} calls
 */
public boolean smoothSlideViewTo(View child, int finalLeft, int finalTop) {
    mCapturedView = child;
    mActivePointerId = INVALID_POINTER;

    boolean continueSliding = forceSettleCapturedViewAt(finalLeft, finalTop, 0, 0);
    if (!continueSliding && mDragState == STATE_IDLE && mCapturedView != null) {
        // If we're in an IDLE state to begin with and aren't moving anywhere, we
        // end up having a non-null capturedView with an IDLE dragState
        mCapturedView = null;
    }

    return continueSliding;
}
可以看到它不受mReleaseInProgress的限制，所以可以在任何地方调用，效果和settleCapturedViewAt()类似，因为它们最终都调用了forceSettleCapturedViewAt()来启动自动滚动，区别在于settleCapturedViewAt()会以最后松手前的滑动速率为初速度将View滚动到最终位置，而smoothSlideViewTo()滚动的初速度是0。forceSettleCapturedViewAt()里有地方调用了Callback里的方法，所以再来看看这个方法：

/**
 * Settle the captured view at the given (left, top) position.
 *
 * @param finalLeft Target left position for the captured view
 * @param finalTop Target top position for the captured view
 * @param xvel Horizontal velocity
 * @param yvel Vertical velocity
 * @return true if animation should continue through {@link #continueSettling(boolean)} calls
 */
private boolean forceSettleCapturedViewAt(int finalLeft, int finalTop, int xvel, int yvel) {
    final int startLeft = mCapturedView.getLeft();
    final int startTop = mCapturedView.getTop();
    final int dx = finalLeft - startLeft;
    final int dy = finalTop - startTop;

    if (dx == 0 && dy == 0) {
        // Nothing to do. Send callbacks, be done.
        mScroller.abortAnimation();
        setDragState(STATE_IDLE);
        return false;
    }

    final int duration = computeSettleDuration(mCapturedView, dx, dy, xvel, yvel);
    mScroller.startScroll(startLeft, startTop, dx, dy, duration);

    setDragState(STATE_SETTLING);
    return true;
}
可以看到自动滑动是靠Scroll类完成，在这里生成了调用mScroller.startScroll()需要的参数。再来看看计算滚动时间的方法computeSettleDuration()：

private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
    xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
    yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
    final int absDx = Math.abs(dx);
    final int absDy = Math.abs(dy);
    final int absXVel = Math.abs(xvel);
    final int absYVel = Math.abs(yvel);
    final int addedVel = absXVel + absYVel;
    final int addedDistance = absDx + absDy;

    final float xweight = xvel != 0 ? (float) absXVel / addedVel :
            (float) absDx / addedDistance;
    final float yweight = yvel != 0 ? (float) absYVel / addedVel :
            (float) absDy / addedDistance;

    int xduration = computeAxisDuration(dx, xvel, mCallback.getViewHorizontalDragRange(child));
    int yduration = computeAxisDuration(dy, yvel, mCallback.getViewVerticalDragRange(child));

    return (int) (xduration * xweight + yduration * yweight);
}
clampMag()方法确保参数中给定的速率在正常范围之内。最终的滚动时间还要经过computeAxisDuration()算出来，通过它的参数可以看到最终的滚动时间是由dx、xvel、mCallback.getViewHorizontalDragRange()共同影响的。看computeAxisDuration()：

private int computeAxisDuration(int delta, int velocity, int motionRange) {
    if (delta == 0) {
        return 0;
    }

    final int width = mParentView.getWidth();
    final int halfWidth = width / 2;
    final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
    final float distance = halfWidth + halfWidth *
            distanceInfluenceForSnapDuration(distanceRatio);

    int duration;
    velocity = Math.abs(velocity);
    if (velocity > 0) {
        duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
    } else {
        final float range = (float) Math.abs(delta) / motionRange;
        duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
    }
    return Math.min(duration, MAX_SETTLE_DURATION);
}
6~10行没看明白，直接看14~19行，如果给定的速率velocity不为0，就通过距离除以速率来算出时间；如果velocity为0，就通过要滑动的距离（delta）除以总的移动范围（motionRange，就是Callback里getViewHorizontalDragRange()、getViewVerticalDragRange()返回值）来算出时间。最后还会对计算出的时间做过滤，最终时间反正是不会超过MAX_SETTLE_DURATION的，源码里的取值是600毫秒，所以不用担心在Callback里getViewHorizontalDragRange()、getViewVerticalDragRange()返回错误的数而导致自动滚动时间过长了。

在调用settleCapturedViewAt()、flingCapturedView()和smoothSlideViewTo()时，还需要实现mParentView的computeScroll()：

@Override
public void computeScroll() {
	if (mDragHelper.continueSettling(true)) {
		ViewCompat.postInvalidateOnAnimation(this);
	}
}
这属于Scroll类用法的范畴，不明白的请参阅Android中滑屏实现----手把手教你如何实现触摸滑屏以及Scroller类详解 的“知识点二： computeScroll(）方法介绍”。

至此，整个触摸流程和ViewDragHelper的重要的方法都过了一遍。之前在讨论shouldInterceptTouchEvent()的ACTION_DOWN部分执行完后应该再执行什么的时候，还有一种情况没有展开详解，就是有子View消费了本次ACTION_DOWN事件的情况，现在来看看这种情况。

假设现在shouldInterceptTouchEvent()的ACTION_DOWN部分执行完了，也有子View消费了这次的ACTION_DOWN事件，那么接下来就会调用mParentView的onInterceptTouchEvent()的ACTION_MOVE部分，不明白为什么的请参阅Andriod 从源码的角度详解View,ViewGroup的Touch事件的分发机制，接着调用ViewDragHelper的shouldInterceptTouchEvent()的ACTION_MOVE部分：

public boolean shouldInterceptTouchEvent(MotionEvent ev) {
	// 省略...
	
    switch (action) {
        // 省略其他case...

        case MotionEvent.ACTION_MOVE: {
            // First to cross a touch slop over a draggable view wins. Also report edge drags.
            final int pointerCount = MotionEventCompat.getPointerCount(ev);
            for (int i = 0; i < pointerCount; i++) {
                final int pointerId = MotionEventCompat.getPointerId(ev, i);
                final float x = MotionEventCompat.getX(ev, i);
                final float y = MotionEventCompat.getY(ev, i);
                final float dx = x - mInitialMotionX[pointerId];
                final float dy = y - mInitialMotionY[pointerId];

                final View toCapture = findTopChildUnder((int) x, (int) y);
                final boolean pastSlop = toCapture != null && checkTouchSlop(toCapture, dx, dy);
                if (pastSlop) {
                    // check the callback's
                    // getView[Horizontal|Vertical]DragRange methods to know
                    // if you can move at all along an axis, then see if it
                    // would clamp to the same value. If you can't move at
                    // all in every dimension with a nonzero range, bail.
                    final int oldLeft = toCapture.getLeft();
                    final int targetLeft = oldLeft + (int) dx;
                    final int newLeft = mCallback.clampViewPositionHorizontal(toCapture,
                            targetLeft, (int) dx);
                    final int oldTop = toCapture.getTop();
                    final int targetTop = oldTop + (int) dy;
                    final int newTop = mCallback.clampViewPositionVertical(toCapture, targetTop,
                            (int) dy);
                    final int horizontalDragRange = mCallback.getViewHorizontalDragRange(
                            toCapture);
                    final int verticalDragRange = mCallback.getViewVerticalDragRange(toCapture);
                    if ((horizontalDragRange == 0 || horizontalDragRange > 0
                            && newLeft == oldLeft) && (verticalDragRange == 0
                            || verticalDragRange > 0 && newTop == oldTop)) {
                        break;
                    }
                }
                reportNewEdgeDrags(dx, dy, pointerId);
                if (mDragState == STATE_DRAGGING) {
                    // Callback might have started an edge drag
                    break;
                }

                if (pastSlop && tryCaptureViewForDrag(toCapture, pointerId)) {
                    break;
                }
            }
            saveLastMotion(ev);
            break;
        }

		// 省略其他case...
    }

    return mDragState == STATE_DRAGGING;
}
如果有多个手指触摸到屏幕上了，对每个触摸点都检查一下，看当前触摸的地方是否需要捕获某个View。这里先用findTopChildUnder(int x, int y)寻找触摸点处的子View，再用checkTouchSlop(View child, float dx, float dy)检查当前触摸点到ACTION_DOWN触摸点的距离是否达到了mTouchSlop，达到了才会去捕获View。
接着看19~41行if (pastSlop){...}部分，这里检查在某个方向上是否可以进行拖动，检查过程涉及到getView[Horizontal|Vertical]DragRange和clampViewPosition[Horizontal|Vertical]四个方法。如果getView[Horizontal|Vertical]DragRange返回都是0，就会认作是不会产生拖动。clampViewPosition[Horizontal|Vertical]返回的是被捕获的View的最终位置，如果和原来的位置相同，说明我们没有期望它移动，也就会认作是不会产生拖动的。不会产生拖动就会在39行直接break，不会执行后续的代码，而后续代码里有调用tryCaptureViewForDrag()，所以不会产生拖动也就不会去捕获View了，拖动也不会进行了。
如果检查到可以在某个方向上进行拖动，就会调用后面的tryCaptureViewForDrag()捕获子View，如果捕获成功，mDragState就会变成STATE_DRAGGING，shouldInterceptTouchEvent()返回true，mParentView的onInterceptTouchEvent()返回true，后续的移动事件就会在mParentView的onTouchEvent()执行了，最后执行的就是mParentView的processTouchEvent()的ACTION_MOVE部分，拖动正常进行。

回头再看之前在shouldInterceptTouchEvent()的ACTION_DOWN部分留下的坑：

public boolean shouldInterceptTouchEvent(MotionEvent ev) {
	// 省略其他部分...
	
    switch (action) {
        // 省略其他case...

        case MotionEvent.ACTION_DOWN: {
			// 省略其他部分...
			
            // Catch a settling view if possible.
            if (toCapture == mCapturedView && mDragState == STATE_SETTLING) {
                tryCaptureViewForDrag(toCapture, pointerId);
            }
			
			// 省略其他部分...
        }

		// 省略其他case...
    }

    return mDragState == STATE_DRAGGING;
}
现在应该明白这部分代码会在什么情况下执行了。当我们松手后捕获的View处于自动滚动的过程中时，用户再次触摸屏幕，就会执行这里的tryCaptureViewForDrag()尝试捕获View，如果捕获成功，mDragState就变为STATE_DRAGGING了，shouldInterceptTouchEvent()就返回true了，然后就是mParentView的onInterceptTouchEvent()返回true，接着执行mParentView的onTouchEvent()，再执行processTouchEvent()的ACTION_DOWN部分。此时（ACTION_DOWN事件发生时）mParentView的onTouchEvent()要返回true，onTouchEvent()才能继续接受到接下来的ACTION_MOVE、ACTION_UP等事件，否则无法完成拖动。

至此整个事件传递流程和ViewDragHelper的重要方法基本都解析完了，shouldInterceptTouchEvent()和processTouchEvent()的ACTION_POINTER_DOWN、ACTION_POINTER_UP部分就留给读者自己解析了。

总结
对于整个触摸事件传递过程，我画了简要的流程图，方便日后快速回顾。

单点触摸，没有考虑边缘滑动检测的最简流程图
单点触摸，考虑了边缘滑动检测的流程图
多点触摸情况我就没研究了，在这里忽略~

三个开启自动滚动的方法：

settleCapturedViewAt(int finalLeft, int finalTop)
以松手前的滑动速度为初速动，让捕获到的View自动滚动到指定位置。只能在Callback的onViewReleased()中调用。
flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop)
以松手前的滑动速度为初速动，让捕获到的View在指定范围内fling。只能在Callback的onViewReleased()中调用。
smoothSlideViewTo(View child, int finalLeft, int finalTop)
指定某个View自动滚动到指定的位置，初速度为0，可在任何地方调用。
Callback的各个方法总结：

void onViewDragStateChanged(int state)
拖动状态改变时会调用此方法，状态state有STATE_IDLE、STATE_DRAGGING、STATE_SETTLING三种取值。
它在setDragState()里被调用，而setDragState()被调用的地方有

tryCaptureViewForDrag()成功捕获到子View时

shouldInterceptTouchEvent()的ACTION_DOWN部分捕获到
shouldInterceptTouchEvent()的ACTION_MOVE部分捕获到
processTouchEvent()的ACTION_MOVE部分捕获到
调用settleCapturedViewAt()、smoothSlideViewTo()、flingCapturedView()时
拖动View松手时（processTouchEvent()的ACTION_UP、ACTION_CANCEL）
自动滚动停止时（continueSettling()里检测到滚动结束时）
外部调用abort()时

void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
正在被拖动的View或者自动滚动的View的位置改变时会调用此方法。

在dragTo()里被调用（正在被拖动时）
在continueSettling()里被调用（自动滚动时）
外部调用abort()时被调用
void onViewCaptured(View capturedChild, int activePointerId)
tryCaptureViewForDrag()成功捕获到子View时会调用此方法。

在shouldInterceptTouchEvent()的ACTION_DOWN里成功捕获
在shouldInterceptTouchEvent()的ACTION_MOVE里成功捕获
在processTouchEvent()的ACTION_MOVE里成功捕获
手动调用captureChildView()
void onViewReleased(View releasedChild, float xvel, float yvel)
拖动View松手时（processTouchEvent()的ACTION_UP）或被父View拦截事件时（processTouchEvent()的ACTION_CANCEL）会调用此方法。
void onEdgeTouched(int edgeFlags, int pointerId)
ACTION_DOWN或ACTION_POINTER_DOWN事件发生时如果触摸到监听的边缘会调用此方法。edgeFlags的取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM的组合。
boolean onEdgeLock(int edgeFlags)
返回true表示锁定edgeFlags对应的边缘，锁定后的那些边缘就不会在onEdgeDragStarted()被通知了，默认返回false不锁定给定的边缘，edgeFlags的取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM其中之一。
void onEdgeDragStarted(int edgeFlags, int pointerId)
ACTION_MOVE事件发生时，检测到开始在某些边缘有拖动的手势，也没有锁定边缘，会调用此方法。edgeFlags取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM的组合。可在此手动调用captureChildView()触发从边缘拖动子View的效果。
int getOrderedChildIndex(int index)
在寻找当前触摸点下的子View时会调用此方法，寻找到的View会提供给tryCaptureViewForDrag()来尝试捕获。如果需要改变子View的遍历查询顺序可改写此方法，例如让下层的View优先于上层的View被选中。
int getViewHorizontalDragRange(View child)、int getViewVerticalDragRange(View child)
返回给定的child在相应的方向上可以被拖动的最远距离，默认返回0。ACTION_DOWN发生时，若触摸点处的child

