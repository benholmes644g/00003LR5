package com.chatt.custom;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by mithramedia on 11/06/16.
 */




// TODO: Auto-generated Javadoc
/**
 * The Class CustomFragment is the base Fragment class. You can extend your
 * Fragment classes with this class in case you want to apply common set of
 * rules for those Fragments.
 */
public class CustomFragmentNew extends Fragment implements View.OnClickListener
{

    /**
     * Set the touch and click listener for a View.
     *
     * @param v
     *            the view
     * @return the same view
     */
    public View setTouchNClick(View v)
    {

        v.setOnClickListener(this);
        v.setOnTouchListener(CustomActivityNew.TOUCH);
        return v;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {

    }

}

