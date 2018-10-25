package edu.uw.viewpager

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import edu.uw.fragmentdemo.SearchFragment

class MainActivity() : AppCompatActivity(), MovieListFragment.OnMovieSelectedListener, SearchFragment.OnSearchListener, Parcelable {
    private var counter:Int = 1;
    private lateinit var viewPager:ViewPager;
    private inner class MoviePagerAdapter(val fm: FragmentManager): FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int {
            return counter;
        }

        override fun getItemPosition(`object`: Any?): Int {
            return POSITION_NONE;
        }

        override fun getItem(position: Int): Fragment {
            if(position == 0) {
                return fm.findFragmentByTag("SearchFragment");
            } else if (position == 1) {
                return fm.findFragmentByTag("MovieListFragment");
            } else if (position == 2){
                return fm.findFragmentByTag("DetailFragment");
            }
        }
    }
    override fun onSearchSubmitted(searchTerm: String) {
        MovieListFragment.newInstance(searchTerm);
        viewPager.adapter.notifyDataSetChanged();
        counter = 1;
        viewPager.setCurrentItem(counter, true);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager);
        val searchFragment:SearchFragment = SearchFragment.newInstance();
        viewPager.adapter = MoviePagerAdapter(supportFragmentManager);
    }

    //respond to search button clicking
    fun handleSearchClick(v: View) {
        val text = findViewById<View>(R.id.txt_search) as EditText
        val searchTerm = text.text.toString()

        val fragment = MovieListFragment.newInstance(searchTerm)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment, MOVIE_LIST_FRAGMENT_TAG)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onMovieSelected(movie: Movie) {
        val fragment = DetailFragment.newInstance(movie)
        viewPager.adapter.notifyDataSetChanged();
        counter = 2;
        viewPager.setCurrentItem(counter, true);
    }

    companion object {

        private val TAG = "MainActivity"
        val MOVIE_LIST_FRAGMENT_TAG = "MoviesListFragment"
        val MOVIE_DETAIL_FRAGMENT_TAG = "DetailFragment"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}
