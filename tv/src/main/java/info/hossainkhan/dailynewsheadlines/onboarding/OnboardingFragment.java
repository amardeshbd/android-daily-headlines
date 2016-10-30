/*
 * MIT License
 *
 * Copyright (c) 2016 Hossain Khan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.hossainkhan.dailynewsheadlines.onboarding;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import info.hossainkhan.android.core.util.PreferenceUtils;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.browser.MainActivity;

public class OnboardingFragment extends android.support.v17.leanback.app.OnboardingFragment {

    private static final int[] pageTitles = {
            R.string.onboarding_title_welcome,
            R.string.onboarding_title_contribute,
            R.string.onboarding_title_relax
    };
    private static final int[] pageDescriptions = {
            R.string.onboarding_description_welcome,
            R.string.onboarding_description_contribute,
            R.string.onboarding_description_relax
    };

    private static final long ANIMATION_DURATION = 500;
    private Animator mContentAnimator;
    private ImageView mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set the logo to display a splash animation
        setLogoResourceId(R.drawable.headlines_splash_banner);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // Our onboarding is done, mark it as completed
        PreferenceUtils.updateOnboardingAsCompleted(getActivity().getApplicationContext());

        // Let's go back to the MainActivity
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    protected int getPageCount() {
        return pageTitles.length;
    }

    @Override
    protected String getPageTitle(int pageIndex) {
        return getString(pageTitles[pageIndex]);
    }

    @Override
    protected String getPageDescription(int pageIndex) {
        return getString(pageDescriptions[pageIndex]);
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        View bgView = new View(getActivity());
        bgView.setBackgroundColor(getResources().getColor(R.color.fastlane_background));
        return bgView;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        mContentView = new ImageView(getActivity());
        mContentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mContentView.setPadding(0, 32, 0, 32);
        return mContentView;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

}