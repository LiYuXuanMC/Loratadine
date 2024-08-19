package net.moran.loratadine.event.impl;


import net.moran.loratadine.event.Event;

public class MotionEvent implements Event {
    private Side side;


    public MotionEvent(Side side) {
        this.side = side;
    }

    public Event.Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
