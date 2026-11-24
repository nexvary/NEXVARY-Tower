package com.nexvary.imsi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Minimal, read-only provider for PDF reports generated in this app's cache.
 * Uses platform APIs only, so the project has no AndroidX dependency.
 */
public final class ReportFileProvider extends ContentProvider {
    private static final String PATH = "report";

    public static Uri uriFor(Context context, File file) {
        File reports = new File(context.getCacheDir(), "reports");
        try {
            String base = reports.getCanonicalPath() + File.separator;
            String candidate = file.getCanonicalPath();
            if (!candidate.startsWith(base)) throw new IllegalArgumentException("Report is outside cache/reports");
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException("Invalid report path", e);
        }
        return new Uri.Builder()
                .scheme("content")
                .authority(context.getPackageName() + ".files")
                .appendPath(PATH)
                .appendPath(file.getName())
                .build();
    }

    @Override public boolean onCreate() { return true; }

    private File resolve(Uri uri) throws FileNotFoundException {
        if (getContext() == null || uri.getPathSegments().size() != 2 || !PATH.equals(uri.getPathSegments().get(0)))
            throw new FileNotFoundException("Invalid report URI");
        String name = uri.getLastPathSegment();
        if (name == null || name.contains("/") || name.contains("\\") || name.contains(".."))
            throw new FileNotFoundException("Invalid report name");
        File reports = new File(getContext().getCacheDir(), "reports");
        File f = new File(reports, name);
        try {
            String base = reports.getCanonicalPath() + File.separator;
            if (!f.getCanonicalPath().startsWith(base) || !f.isFile()) throw new FileNotFoundException("Report not found");
        } catch (java.io.IOException e) { throw new FileNotFoundException("Invalid report path"); }
        return f;
    }

    @Override public String getType(Uri uri) { return "application/pdf"; }

    @Override public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (!"r".equals(mode)) throw new FileNotFoundException("Read only");
        return ParcelFileDescriptor.open(resolve(uri), ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            File f = resolve(uri);
            MatrixCursor c = new MatrixCursor(new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE});
            c.addRow(new Object[]{f.getName(), f.length()});
            return c;
        } catch (FileNotFoundException e) { return null; }
    }

    @Override public Uri insert(Uri uri, ContentValues values) { throw new UnsupportedOperationException("read only"); }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { throw new UnsupportedOperationException("read only"); }
    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { throw new UnsupportedOperationException("read only"); }
}
