package com.example.monikasaini.preview.utility;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.monikasaini.preview.Loader;

/**
 * Created by Monika on 06/06/17.
 */

public class PreviewUtility {

    public static Loader getLoaderOfType(final Context context, int type, final String title, final String message) {
        Loader loader = null;
        if (type == Loader.PROGRESS_DIALOG) {

            loader = new Loader() {

                ProgressDialog pd;

                @Override
                public void showLoader() {
                    try {
                        pd = new ProgressDialog(context);
                        pd.setMessage(message);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setCancelable(false);
                        pd.setTitle(title);
                        pd.show();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void dismissLoader(Runnable run) {
                    try {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                            if (run != null) {
                                run.run();
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            };
        }

        return loader;
    }

}
