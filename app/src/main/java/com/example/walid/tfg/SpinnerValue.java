package com.example.walid.tfg;

/**
 * Created by walid on 03/03/2018.
 */

class SpinnerValue {

    private String text;
    private String value;

    public SpinnerValue() {
    }

    public SpinnerValue(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return text;
    }
}
