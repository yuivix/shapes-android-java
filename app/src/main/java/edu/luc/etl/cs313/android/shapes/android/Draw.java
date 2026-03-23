package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO --Done-- entirely your job (except onCircle) used it as base

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME DONE
        this.paint = paint; // FIXME DONE
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        final int prevColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(prevColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        final Style prevStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(prevStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        final Style prevStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(prevStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final java.util.List<? extends Point> points = s.getPoints();
        final int size = points.size();
        final float[] pts = new float[size * 4];
        for (int i = 0; i < size; i++) {
            final Point p1 = points.get(i);
            final Point p2 = points.get((i + 1) % size);
            pts[i * 4] = p1.getX();
            pts[i * 4 + 1] = p1.getY();
            pts[i * 4 + 2] = p2.getX();
            pts[i * 4 + 3] = p2.getY();
        }
        canvas.drawLines(pts, paint);
        return null;
    }
}
