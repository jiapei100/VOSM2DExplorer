package nl.skybound.awt;
/*
 * Copyright (c) 2001 by Skybound Software, http://www.skybound.nl
 * All rights reserved.
 *
 * This software may be freely copied, modified and redistributed without
 * fee for non-commerical purposes provided that this copyright notice is
 * preserved intact on all copies and modified copies.
 *
 * There is no warranty or other guarantee of fitness of this software.
 * It is provided solely "as is". The author(s) disclaim(s) all
 * responsibility and liability with respect to this software's usage
 * or its effect upon hardware or computer systems.
 */

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Similar to but not a descendent of java.awt.Polygon that uses double values
 * instead of int values.
 *
 * @author Craig Manley
 * @version 1.01
 * @see java.awt.Polygon
 */
public class DoublePolygon implements Serializable {

  /**
   * The total number of points.
   * This value can be NULL.
   *
   * @serial
   * @see #addPoint(double, double)
   */
  public int npoints = 0;

  /**
   * The array of <i>x</i> coordinates.
   *
   * @serial
   * @see #addPoint(double, double)
   */
  public double xpoints[] = new double[4];

  /**
   * The array of <i>y</i> coordinates.
   *
   * @serial
   * @see #addPoint(double, double)
   */
  public double ypoints[] = new double[4];

  /**
   * Creates an empty polygon.
   */
  public DoublePolygon() {
  }

  /**
   * Constructs and initializes a <code>DoublePolygon</code> from the specified
   * parameters.
   * @param xpoints an array of <i>x</i> coordinates
   * @param ypoints an array of <i>y</i> coordinates
   * @param npoints the total number of points in the
   *				<code>Polygon</code>
   * @exception  NegativeArraySizeException if the value of
   *                       <code>npoints</code> is negative.
   * @exception  IndexOutOfBoundsException if <code>npoints</code> is
   *             greater than the length of <code>xpoints</code>
   *             or the length of <code>ypoints</code>.
   * @exception  NullPointerException if <code>xpoints</code> or
   *             <code>ypoints</code> is <code>null</code>.
   */
  public DoublePolygon(final double[] xpoints, final double[] ypoints, final int npoints) {
    this.npoints = npoints;
    this.xpoints = new double[npoints];
    this.ypoints = new double[npoints];
    System.arraycopy(xpoints, 0, this.xpoints, 0, npoints);
    System.arraycopy(ypoints, 0, this.ypoints, 0, npoints);
  }

  /**
   * Translates the vertices of the <code>DoublePolygon</code> by
   * <code>deltaX</code> along the x axis and by
   * <code>deltaY</code> along the y axis.
   * @param deltaX the amount to translate along the <i>x</i> axis
   * @param deltaY the amount to translate along the <i>y</i> axis
   */
  public void translate(final double deltaX, final double deltaY) {
    for (int i = 0; i < npoints; i++) {
      xpoints[i] += deltaX;
      ypoints[i] += deltaY;
    }
  }

  /**
   * Appends the specified coordinates to this <code>DoublePolygon</code>.
   * <p>
   * @param       x,&nbsp;y   the specified coordinates
   * @see         #contains
   */
  public void addPoint(final double x, final double y) {
    if (npoints == xpoints.length) {
      double tmp[];
      tmp = new double[npoints * 2];
      System.arraycopy(xpoints, 0, tmp, 0, npoints);
      xpoints = tmp;
      tmp = new double[npoints * 2];
      System.arraycopy(ypoints, 0, tmp, 0, npoints);
      ypoints = tmp;
    }
    xpoints[npoints] = x;
    ypoints[npoints] = y;
    npoints++;
  }
  
  public void addPoint(Point2D pt) {
	  this.addPoint(pt.getX(), pt.getY());
	  }

  /**
   * Determines whether the specified coordinates are inside this
   * <code>DoublePolygon</code>.  For the definition of
   * <i>insideness</i>, see the class comments of {@link Shape}.
   * @param x,&nbsp;y the specified coordinates
   * @return <code>true</code> if this <code>Polygon</code> contains the
   * specified coordinates; <code>false</code> otherwise.
   */
  /*
  public boolean contains(final double x, final double y) {
    if (npoints < 3) {
      return false;
    }
    int intersections = 0; // If line intersections is odd, then point in polygon.
    double minX = getMinX();
    double minY = getMinY();
    for (int i=0; i < npoints; i++) {
      int nextindex;
      if (i < npoints-1) {
        nextindex = i+1;
      }
      else {
        nextindex = 0;
      }
      if (intersects(xpoints[i],ypoints[i],xpoints[nextindex],ypoints[nextindex],minX,minY,x,y)) {
        intersections++;
      }
    }
    return ((intersections & 1) != 0);
  }*/

  /**
   * Determines whether the specified coordinates are inside this
   * <code>Polygon</code>.  For the definition of
   * <i>insideness</i>, see the class comments of {@link Shape}.
   * @param x,&nbsp;y the specified coordinates
   * @return <code>true</code> if this <code>Polygon</code> contains the
   * specified coordinates; <code>false</code> otherwise.
   */
  public boolean contains(double x, double y) {
    if (npoints < 3) {
      return false;
    }
    int hits = 0;
    double lastx = xpoints[npoints - 1];
    double lasty = ypoints[npoints - 1];
    double curx, cury;

    // Walk the edges of the polygon
    for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
      curx = xpoints[i];
      cury = ypoints[i];
      if (cury == lasty) {
  	continue;
      }

      double leftx;
      if (curx < lastx) {
	if (x >= lastx) {
	  continue;
	}
      	leftx = curx;
      }
      else {
	if (x >= curx) {
	  continue;
        }
        leftx = lastx;
      }

      double test1, test2;
      if (cury < lasty) {
        if (y < cury || y >= lasty) {
  	  continue;
        }
        if (x < leftx) {
          hits++;
          continue;
        }
        test1 = x - curx;
        test2 = y - cury;
      }
      else {
        if (y < lasty || y >= cury) {
	  continue;
        }
        if (x < leftx) {
          hits++;
          continue;
        }
        test1 = x - lastx;
        test2 = y - lasty;
      }
      if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
        hits++;
      }
    }
    return ((hits & 1) != 0);
  }

  public boolean contains(Point2D pt) {
	    return (this.contains(pt.getX(), pt.getY()));
	  }
  
  public Point2D getAt(int index)
  {
	  Point2D res = new Point2D.Double(xpoints[index], ypoints[index]);
	  return res;
  }
  
  /**
   * Returns the minimum x coordinate.
   */
  public double getMinX() {
    double result = Double.MAX_VALUE;
    for (int i=0; i < npoints; i++) {
      if (xpoints[i] < result) {
        result = xpoints[i];
      }
    }
    return result;
  }

  /**
   * Returns the minimum y coordinate.
   */
  public double getMinY() {
    double result = Double.MAX_VALUE;
    for (int i=0; i < npoints; i++) {
      if (ypoints[i] < result) {
        result = ypoints[i];
      }
    }
    return result;
  }

  /**
   * Returns the maximum x coordinate.
   */
  public double getMaxX() {
    double result = Double.MIN_VALUE;
    for (int i=0; i < npoints; i++) {
      if (xpoints[i] > result) {
        result = xpoints[i];
      }
    }
    return result;
  }

  /**
   *  Returns the maximum y coordinate.
   */
  public double getMaxY() {
    double result = Double.MIN_VALUE;
    for (int i=0; i < npoints; i++) {
      if (ypoints[i] > result) {
        result = ypoints[i];
      }
    }
    return result;
  }

  /**
   * Determines if two lines intersect.
   *
   * @param ax,&nbsp;ay the origin coordinates of line 1.
   * @param bx,&nbsp;by the end coordinates of line 1.
   * @param cx,&nbsp;cy the origin coordinates of line 2.
   * @param dx,&nbsp;dy the end coordinates of line 2.
   */
  /*
  private boolean intersects(final double ax, final double ay, final double bx, final double by,
                             final double cx, final double cy, final double dx, final double dy) {
    /* Let A,B,C,D be 2-space position vectors.  Then the directed line
       segments AB & CD are given by:

       AB=A+r(B-A), r in [0,1]
       CD=C+s(D-C), s in [0,1]

       If AB & CD intersect, then

       A+r(B-A)=C+s(D-C), or

       Ax+r(Bx-Ax)=Cx+s(Dx-Cx)
       Ay+r(By-Ay)=Cy+s(Dy-Cy)  for some r,s in [0,1]

           (Ay-Cy)(Dx-Cx)-(Ax-Cx)(Dy-Cy)
       r = -----------------------------  (eqn 1)
           (Bx-Ax)(Dy-Cy)-(By-Ay)(Dx-Cx)
    */
    /*
    double denominator = (bx-ax) * (dy-cy) - (by-ay) * (dx-cx);
    if (denominator == 0) {
      return false;
    }
    double r = ((ay-cy) * (dx-cx) - (ax-cx) * (dy-cy)) / denominator;
    return ((r >= 0) && (r <= 1));
  }*/
}