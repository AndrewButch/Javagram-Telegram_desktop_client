package Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

/**
 * Код взять с https://stackoverflow.com/questions/16598439/scrolling-on-jlist-without-the-scrollbars
 */

public class MyMouseWheelScroller extends MouseAdapter {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //  Ignore events generated with a rotation of 0
        //  (not sure why these events are generated)

        int rotation = e.getWheelRotation();

        if (rotation == 0)
            return;

        //  Get the appropriate Action key for the given rotation
        //  (unit/block scroll is system dependent)

        String key = null;

        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
        {
            key = (rotation < 0) ? "negativeUnitIncrement" : "positiveUnitIncrement";
        }
        else
        {
            key = (rotation < 0) ? "negativeBlockIncrement" : "positiveBlockIncrement";
        }

        //  Get the Action from the scrollbar ActionMap for the given key

        JScrollPane scrollPane = (JScrollPane)e.getComponent();
        JScrollBar vertical = scrollPane.getVerticalScrollBar();

        ActionMap map = vertical.getActionMap();
        Action action = (Action)map.get( key );
        ActionEvent event = new ActionEvent(vertical, ActionEvent.ACTION_PERFORMED, "");

        //  Invoke the Action the appropriate number of times to simulate
        //  default mouse wheel scrolling

        int unitsToScroll = Math.abs( e.getUnitsToScroll() );

        for (int i = 0; i < unitsToScroll; i++)
            action.actionPerformed( event );
    }
}
