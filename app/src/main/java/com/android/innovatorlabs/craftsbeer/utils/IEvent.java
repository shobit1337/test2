package com.android.innovatorlabs.craftsbeer.utils;

import java.io.Serializable;

public final class IEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventName;

    private final int eventID;

    private final Object object;

    private IEvent(Builder aBuilder) {

        eventName = aBuilder.eventName;
        eventID = aBuilder.eventID;
        object = aBuilder.object;
    }

    public String getEventName() {
        return eventName;
    }

    public int getEventID() {
        return eventID;
    }

    public Object getEventObject() {
        return object;
    }

    public static final class Builder {

        private int eventID = -1;

        private String eventName = "";

        private Object object = null;

        public Builder setEventName( String eventName ) {
            this.eventName = eventName;
            return this;
        }

        public Builder setEventID( int eventID ) {
            this.eventID = eventID;
            return this;
        }

        public Builder setEventObject( Object object ) {
            this.object = object;
            return this;
        }

        public IEvent build() {
            return new IEvent(this);
        }
    }

}


