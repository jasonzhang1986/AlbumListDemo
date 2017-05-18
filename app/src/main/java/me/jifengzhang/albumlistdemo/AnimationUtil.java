package me.jifengzhang.albumlistdemo;

import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author linan1 2013年11月15日 下午7:02:58
 */
public class AnimationUtil {

	/**
	 * 播放放大动画
	 * 
	 * @param view
	 */
	public static AnimatorSet startScaleToBigAnimation(final View view, float rate, AnimatorListener animatorListener) {
		if (null != view.getAnimation()) {
			
			view.getAnimation().cancel();
		}
		view.clearAnimation();
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, rate);
		oa.setDuration(100);
		oa.setInterpolator(new DecelerateInterpolator());

		ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, rate);
		oa1.setDuration(100);
		oa1.setInterpolator(new DecelerateInterpolator());
		set.playTogether(oa, oa1);
		if (null != animatorListener) {
			set.addListener(animatorListener);
		}

		set.start();

		return set;

	}
	
	/**
	 * 播放缩小动画
	 * 
	 * @param view
	 */
	public static AnimatorSet startScaleToSmallAnimation(View view, float rate, AnimatorListener animatorListener) {
		if (null != view.getAnimation()) {
			view.getAnimation().cancel();
		}
		view.clearAnimation();
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator oa = ObjectAnimator.ofFloat(view, "scaleY", rate, 1.0f);
		oa.setDuration(100);
		oa.setInterpolator(new DecelerateInterpolator());

		ObjectAnimator oa1 = ObjectAnimator.ofFloat(view, "scaleX", rate, 1.0f);
		oa1.setDuration(100);
		oa1.setInterpolator(new DecelerateInterpolator());

		set.playTogether(oa, oa1);
		set.start();
		return set;
	}
	
}
