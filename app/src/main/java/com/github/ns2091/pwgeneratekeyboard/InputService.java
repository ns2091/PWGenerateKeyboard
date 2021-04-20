package com.github.ns2091.pwgeneratekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.annotation.RequiresApi;

public class InputService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;

    private boolean caps = false;
    private boolean lower = false;
    private boolean upper = false;
    private boolean digit = false;
    private boolean symbol = false;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.key_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    public InputService() {
        super();
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onKey(int i, int[] ints) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            switch(i) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
                case Keyboard.KEYCODE_SHIFT:
                    caps = !caps;
                    keyboard.setShifted(caps);
                    keyboardView.invalidateAllKeys();
                    break;
                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;
                case -901:
                    lower = !lower;
                    break;
                case -902:
                    upper = !upper;
                    break;
                case -903:
                    digit = !digit;
                    break;
                case -904:
                    symbol = !symbol;
                    break;
                default:
                    int raw_code = Integer.parseInt(String.valueOf(i));
                    int code = Math.abs(raw_code);
                    int pass_length = code - 1000;
                    PasswordGenerator passwordGenerator;
                    if (lower || upper || digit || symbol) {
                        passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                                .useDigits(digit)
                                .useLower(lower)
                                .useUpper(upper)
                                .usePunctuation(symbol)
                                .build();
                    } else {
                        passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                                .useDigits(true)
                                .useLower(true)
                                .useUpper(true)
                                .usePunctuation(true)
                                .build();
                    }
                    String password = passwordGenerator.generate(pass_length);
                    inputConnection.commitText(password, 1);
            }
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}