package com.claro.cfcmobile.db.contracts;

import android.net.Uri;
import com.tjeannin.provigen.Constraint;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.*;

/**
 * Created by Christopher Herrera on 3/7/14.
 */
@Contract(version = 1)
public interface DispatchItemsContract extends ProviGenBaseContract {

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String CODE = "CODE";

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String KEY  = "KEY";

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String VALUE  = "VALUE";


    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://com.claro.cfcmobile/dispatchitems");
}
