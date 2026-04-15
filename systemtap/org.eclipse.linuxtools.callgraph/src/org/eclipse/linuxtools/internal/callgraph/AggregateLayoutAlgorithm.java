/*******************************************************************************
 * Copyright (c) 2009, 2018 Red Hat, Inc.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.linuxtools.internal.callgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.interfaces.EntityLayout;

/**
 * Calculates the size and location of a node when rendering the
 * Aggregate View. This avoids needing to design a Layout Algorithm
 * from scratch.
 */
public class AggregateLayoutAlgorithm extends GridLayoutAlgorithm {

    private final List<Long> list;
    private final long totalTime;
    private final int graphWidth;


    /**
     * Layout algorithm for the Aggregate View in Eclipse Callgraph, based on the GridLayoutAlgorithm in Zest.
     * @param styles
     * @param entries
     * @param time
     * @param width
     */
    public AggregateLayoutAlgorithm(TreeSet<Entry<String, Long>> entries, Long time, int width){
        super();
        setResizing(true);

        list = new ArrayList<>();
        for (Entry<String, Long> ent : entries) {
            list.add(ent.getValue());
        }

        this.totalTime = time;
        this.graphWidth = width;
    }

    /**
     * Called at the end of the layout algorithm -- change the size and colour
     * of each node according to times called/total time
     */
    @Override
    public void applyLayout(boolean clean) {
        super.applyLayout(clean);
        if (!clean || context == null) {
            return;
        }

        EntityLayout[] entitiesToLayout = context.getEntities();
        int timedEntities = Math.min(entitiesToLayout.length, list.size());
        final int minimumSize = 40;
        double xcursor = 0.0;
        double ycursor = 0.0;

        for (int i = 0; i < entitiesToLayout.length; i++) {
            EntityLayout sn = entitiesToLayout[i];
            long time = i < timedEntities ? list.get(i) : 0L;
            double percent = totalTime == 0L ? 0.0 : (double) time / (double) totalTime;
            double snWidth = (sn.getSize().width * percent) + minimumSize;
            double snHeight = (sn.getSize().height * percent) + minimumSize;


            if (sn.isResizable()) {
                sn.setSize(snWidth, snHeight);
            }
            double x;
            double y;
            if (xcursor + snWidth > graphWidth) {
                //reaching the end of row, move to lower column
                ycursor += snHeight;
                xcursor = 0;
                x = xcursor;
                y = ycursor;
            } else {
                x = xcursor;
                y = ycursor;
                xcursor += snWidth;
            }
            if (sn.isMovable()) {
                sn.setLocation(x, y);
            }
        }
    }

}
