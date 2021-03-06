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

package info.hossainkhan.dailynewsheadlines


import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import info.hossainkhan.android.core.headlines.HeadlinesContract
import info.hossainkhan.android.core.headlines.HeadlinesPresenter
import info.hossainkhan.android.core.model.NewsHeadlineItem
import info.hossainkhan.android.core.model.NewsCategoryHeadlines
import info.hossainkhan.android.core.model.NewsHeadlines
import info.hossainkhan.android.core.model.ScreenType
import info.hossainkhan.android.core.newsprovider.NewsProviderManager
import kotlinx.android.synthetic.main.activity_headlines_nav_and_content.*
import kotlinx.android.synthetic.main.headlines_item_viewpager_container.*
import kotlinx.android.synthetic.main.headlines_main_content_container.*
import kotlinx.android.synthetic.main.nav_header_main.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HeadlinesBrowseActivity
    : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        HeadlinesContract.View {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v13.app.FragmentStatePagerAdapter].
     */
    private var headlinesPagerAdapter: HeadlinesPagerAdapter? = null

    private lateinit var headlinesPresenter: HeadlinesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines_nav_and_content)
        setSupportActionBar(toolbar)
        supportActionBar?.title =  getString(R.string.choose_source_title)

        setupNavigationDrawer()

        // NOTE use DI to inject
        val context = applicationContext
        val newsProviderManager = NewsProviderManager(context)
        headlinesPresenter = HeadlinesPresenter(context, this, newsProviderManager)
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        sidebar_subtitle.text = SimpleDateFormat("EEE, d MMM yyy", Locale.getDefault()).format(Date())
    }

    override fun onStop() {
        // NOTE - What happens when presenter is attached again.
        headlinesPresenter.detachView()
        super.onStop()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_headlines_browse, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val isConsumed = headlinesPresenter.onMenuItemClicked(item)
        return if (isConsumed) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.action_add_news_source_feed -> {
                // Handle the camera action
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    /**
     * Setups the navigation bar with all the news sources which can be selected.
     */
    private fun setupNavigationDrawerAdapter(headlines: List<NewsHeadlines>) {
        nav_drawer_recycler_view.adapter = NewsSourceAdapter(headlines,
                this::onNewsSourceSelected)
    }

    /**
     * Updates toolbar title with currently selected content
     */
    private fun updateToolbarTitle(title: String) {
        toolbar.title = title
    }

    fun onNewsSourceSelected(selectedRow: NewsCategoryHeadlines) {
        Timber.d("onNewsSourceSelected() called with: row = [${selectedRow}]")

        updateToolbarTitle(selectedRow.displayTitle ?: selectedRow.title!!)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        if (headlinesPagerAdapter == null) {
            // Set up the ViewPager with the sections adapter.
            headlinesPagerAdapter = HeadlinesPagerAdapter(fragmentManager, selectedRow.newsHeadlines!!)
            news_headlines_pager_container.adapter = headlinesPagerAdapter
        } else {
            headlinesPagerAdapter!!.setData(selectedRow.newsHeadlines!!)
            news_headlines_pager_container.setCurrentItem(0, true)
        }

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    //
    // HeadlinesContract.View
    //
    override fun showHeadlines(headlines: MutableList<NewsHeadlines>) {
        Timber.d("showHeadlines() called with: categoriesHeadlines = [${headlines}]")
        setupNavigationDrawerAdapter(headlines)
    }

    override fun showHeadlineDetailsUi(newsHeadlineItem: NewsHeadlineItem?) {
        Timber.d("showHeadlineDetailsUi() called with: newsHeadlineItem = [${newsHeadlineItem}]")
        // NOTE: Details view on mobile is not supported to keep it minimal.
    }

    override fun showAppSettingsScreen() {
        Timber.d("showAppSettingsScreen() called")
        Toast.makeText(this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show()
    }

    override fun showHeadlineBackdropBackground(imageUrl: String?) {
        Timber.d("showHeadlineBackdropBackground() called with: imageUrl = [${imageUrl}]")
    }

    override fun showDefaultBackground() {
        Timber.d("showDefaultBackground() called")
    }

    override fun toggleLoadingIndicator(active: Boolean) {
        Timber.d("toggleLoadingIndicator() called with: active = [${active}]")
        if (active) {
            news_headlines_loading_indicator.visibility = View.VISIBLE
        } else {
            news_headlines_loading_indicator.visibility = View.GONE
        }
    }

    override fun showDataLoadingError() {
        Timber.d("showDataLoadingError() called")

        Toast.makeText(this, "Failed to load news.", Toast.LENGTH_LONG).show()
    }

    override fun showDataNotAvailable() {
        Timber.d("showDataNotAvailable() called")
    }

    override fun showAddNewsSourceScreen() {
        Timber.d("showAddNewsSourceScreen() called")
        Toast.makeText(this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show()
    }

    override fun showUiScreen(type: ScreenType) {
        Timber.d("showUiScreen() called with: type = [${type}]")
        Toast.makeText(this, "Feature not implemented yet.", Toast.LENGTH_SHORT).show()
    }
}

