package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.aqinn.actmanagersysandroid.R;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aqinn
 * @date 2020/12/11 8:30 PM
 */
public class ErrorFragment extends BaseFragment {

    @BindView(R.id.emptyView)
    QMUIEmptyView emptyView;

    private String mTitle = null, mDetail = null;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_error, null);
        ButterKnife.bind(this, root);
        if (mTitle != null)
            emptyView.setTitleText(mTitle);
        if (mDetail != null)
            emptyView.setDetailText(mDetail);
        return root;
    }

    public ErrorFragment(String title, String detail) {
        mTitle = title;
        mDetail = detail;
    }

}
