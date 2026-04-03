package com.capacitor.navigationbar;

import android.view.Window;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "NavigationBar")
public class NavigationBarPlugin extends Plugin {

    private final NavigationBar implementation = new NavigationBar();

    @PluginMethod
    public void hide(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            implementation.hide(getActivity().getWindow());
            notifyListeners("onHide", new JSObject());
            call.resolve();
        });
    }

    @PluginMethod
    public void show(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            implementation.show(getActivity().getWindow());
            notifyListeners("onShow", new JSObject());
            call.resolve();
        });
    }

    @PluginMethod
    public void setTransparency(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            Boolean isTransparent = call.getBoolean("isTransparent", false);
            implementation.setTransparency(getActivity().getWindow(), isTransparent);
            call.resolve();
        });
    }

    @PluginMethod
    public void setColor(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            String color = call.getString("color");
            boolean darkButtons = call.getBoolean("darkButtons", false);

            String newColor = implementation.setColor(getActivity().getWindow(), color, darkButtons);
            if (newColor == null) {
                call.reject("Invalid Hexadecimal color");
                return;
            }

            JSObject ret = new JSObject();
            ret.put("color", newColor);
            notifyListeners("onColorChange", ret);
            call.resolve();
        });
    }

    @PluginMethod
    public void getColor(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            JSObject ret = new JSObject();
            ret.put("color", implementation.getColor());
            call.resolve(ret);
        });
    }
}
