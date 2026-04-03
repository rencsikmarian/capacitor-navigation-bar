package com.capacitor.navigationbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import java.util.regex.Pattern;

public class NavigationBar {

    private boolean isTransparent = false;
    private int currentColor = Color.BLACK;
    private View navBarBackground = null;

    private static final int NAV_BAR_VIEW_ID = View.generateViewId();

    public void hide(Window window) {
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        controller.hide(WindowInsetsCompat.Type.navigationBars());
    }

    public void show(Window window) {
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
        controller.show(WindowInsetsCompat.Type.navigationBars());
    }

    public void setTransparency(Window window, boolean isTransparent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WindowCompat.setDecorFitsSystemWindows(window, !isTransparent);
            if (isTransparent) {
                window.setNavigationBarColor(Color.TRANSPARENT);
                window.setNavigationBarContrastEnforced(false);
            } else {
                window.setNavigationBarColor(this.currentColor);
                window.setNavigationBarContrastEnforced(true);
            }
        } else {
            if (isTransparent) {
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
        this.isTransparent = isTransparent;

        if (Build.VERSION.SDK_INT >= 35) {
            updateNavBarBackground(window, isTransparent ? Color.TRANSPARENT : this.currentColor);
        }
    }

    public String setColor(Window window, String color, boolean darkButtons) {
        if (color == null || color.isEmpty()) {
            color = "#FFFFFF";
        }

        if (!validateHexColor(color)) {
            return null;
        }

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
        controller.setAppearanceLightNavigationBars(darkButtons);

        this.currentColor = Color.parseColor(color);

        if (Build.VERSION.SDK_INT >= 35) {
            // API 35+: setNavigationBarColor() is ignored, use a background view instead
            if (!this.isTransparent) {
                updateNavBarBackground(window, this.currentColor);
            }
        } else {
            if (!this.isTransparent) {
                window.setNavigationBarColor(this.currentColor);
            }
        }

        return formatColor(this.currentColor);
    }

    public String getColor() {
        return formatColor(this.currentColor);
    }

    /**
     * On API 35+ (Android 15), setNavigationBarColor() is deprecated and ignored.
     * Instead, we add a colored View behind the navigation bar insets.
     */
    private void updateNavBarBackground(Window window, int color) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();

        if (navBarBackground == null) {
            navBarBackground = new View(decorView.getContext());
            navBarBackground.setId(NAV_BAR_VIEW_ID);
            decorView.addView(navBarBackground);

            ViewCompat.setOnApplyWindowInsetsListener(navBarBackground, (v, insets) -> {
                Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navInsets.bottom);
                params.topMargin = decorView.getHeight() - navInsets.bottom;
                v.setLayoutParams(params);
                return insets;
            });
        }

        navBarBackground.setBackgroundColor(color);
        navBarBackground.requestApplyInsets();
    }

    private String formatColor(int color) {
        String hex = String.format("#%08X", (0xFFFFFFFF & color));
        if (hex.startsWith("#FF")) {
            hex = "#" + hex.substring(3);
        }
        return hex;
    }

    private boolean validateHexColor(String color) {
        if (color == null || color.isEmpty()) {
            return false;
        }
        Pattern hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6})$|^#([A-Fa-f0-9]{8})$");
        return hexPattern.matcher(color).matches();
    }
}
