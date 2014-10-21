package com.claro.cfcmobile.db.contracts;

import android.net.Uri;
import com.tjeannin.provigen.Constraint;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.*;

/**
 * Created by Christopher Herrera on 3/10/14.
 */
@Contract(version = 1)
public interface DIListContract extends ProviGenBaseContract {

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String CODE = "CODE";

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String TECHNOLOGY = "TECHNOLOGY";

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String TYPE = "TYPE";

    @Column(Column.Type.TEXT)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String COMMITMENT_DATE = "COMMITMENTDATE";

    @Column(Column.Type.INTEGER)
    @NotNull(Constraint.OnConflict.ABORT)
    public static final String STATUS = "STATUS";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://com.claro.cfcmobile/dilistcontract");
}
