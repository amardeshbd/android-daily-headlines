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

package info.hossainkhan.dailynewsheadlines.browser;

import android.content.Context;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DividerRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.SectionRow;

import java.util.List;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.cards.CardListRow;
import info.hossainkhan.dailynewsheadlines.cards.presenters.CardPresenterSelector;
import info.hossainkhan.dailynewsheadlines.cards.presenters.TextFeedCardPresenter;


/**
 * Leanback {@link Row} UI builder factory.
 */
public class RowBuilderFactory {

    /**
     * Creates appropriate {@link Row} item based on {@link NavigationRow} type.
     *
     * @param navigationRow Navigation row
     * @return {@link Row}
     */
    public static Row buildCardRow(final Context context, final NavigationRow navigationRow) {
        int navigationRowType = navigationRow.getType();
        switch (navigationRowType) {
            case NavigationRow.TYPE_SECTION_HEADER:
                return new SectionRow(new HeaderItem(navigationRow.getTitle()));
            case NavigationRow.TYPE_DIVIDER:
                return new DividerRow();
            case NavigationRow.TYPE_DEFAULT:
            default:
                // Build rows using different presenter defined in "CardPresenterSelector"
                PresenterSelector presenterSelector = new CardPresenterSelector(context);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenterSelector);
                for (CardItem card : navigationRow.getCards()) {
                    listRowAdapter.add(card);
                }

                HeaderItem header = new HeaderItem(navigationRow.getTitle());

                return new CardListRow(header, listRowAdapter, navigationRow);
        }
    }

    public static Row buildSearchResultCardRow(final Context context, final List<CardItem> cardItems) {
        // Build rows using different presenter defined in "CardPresenterSelector"
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new TextFeedCardPresenter(context));
        for (CardItem card : cardItems) {
            listRowAdapter.add(card);
        }

        HeaderItem header = new HeaderItem(context.getString(R.string.search_result_title, cardItems.size()));

        return new ListRow(header, listRowAdapter);
    }
}
