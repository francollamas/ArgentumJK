package com.argentumjk.client.web;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier;
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.BusyBar;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.ListViewStyle;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.MultiSplitPane;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.color.ColorPickerStyle;
import com.kotcrab.vis.ui.widget.color.ColorPickerWidgetStyle;
import com.kotcrab.vis.ui.widget.file.FileChooserStyle;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;

import java.io.File;
import java.io.IOException;

import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

/**
 * Builds the TeaVM/HTML application.
 */
@SkipClass
public class WebBuilder {
    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration teaBuildConfiguration = new TeaBuildConfiguration();
        teaBuildConfiguration.assetsPath.add(new AssetFileHandle("../assets"));
        teaBuildConfiguration.webappPath = new File("build/dist").getCanonicalPath();

        // Register any extra classpath assets here:
        // teaBuildConfiguration.additionalAssetsClasspathFiles.add("com/argentumjk/client/asset.extension");

        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("com.argentumjk.client.reflect");
        TeaReflectionSupplier.addReflectionClass(Sizes.class);
        TeaReflectionSupplier.addReflectionClass(VisImageButton.VisImageButtonStyle.class);
        TeaReflectionSupplier.addReflectionClass(VisImageTextButton.VisImageTextButtonStyle.class);
        TeaReflectionSupplier.addReflectionClass(VisCheckBox.VisCheckBoxStyle.class);
        TeaReflectionSupplier.addReflectionClass(VisTextField.VisTextFieldStyle.class);
        TeaReflectionSupplier.addReflectionClass(PopupMenu.PopupMenuStyle.class);
        TeaReflectionSupplier.addReflectionClass(Menu.MenuStyle.class);
        TeaReflectionSupplier.addReflectionClass(MenuBar.MenuBarStyle.class);
        TeaReflectionSupplier.addReflectionClass(Separator.SeparatorStyle.class);
        TeaReflectionSupplier.addReflectionClass(VisSplitPane.VisSplitPaneStyle.class);
        TeaReflectionSupplier.addReflectionClass(MultiSplitPane.MultiSplitPaneStyle.class);
        TeaReflectionSupplier.addReflectionClass(MenuItem.MenuItemStyle.class);
        TeaReflectionSupplier.addReflectionClass(Tooltip.TooltipStyle.class);
        TeaReflectionSupplier.addReflectionClass(LinkLabel.LinkLabelStyle.class);
        TeaReflectionSupplier.addReflectionClass(TabbedPane.TabbedPaneStyle.class);
        TeaReflectionSupplier.addReflectionClass(Spinner.SpinnerStyle.class);
        TeaReflectionSupplier.addReflectionClass(FileChooserStyle.class);
        TeaReflectionSupplier.addReflectionClass(ColorPickerWidgetStyle.class);
        TeaReflectionSupplier.addReflectionClass(ColorPickerStyle.class);
        TeaReflectionSupplier.addReflectionClass(BusyBar.BusyBarStyle.class);
        TeaReflectionSupplier.addReflectionClass(ListViewStyle.class);
        TeaReflectionSupplier.addReflectionClass(SimpleFormValidator.FormValidatorStyle.class);

        TeaVMTool tool = TeaBuilder.config(teaBuildConfiguration);
        tool.setMainClass(WebLauncher.class.getName());
        // For many (or most) applications, using the highest optimization won't add much to build time.
        // If your builds take too long, and runtime performance doesn't matter, you can change FULL to SIMPLE .
        tool.setOptimizationLevel(TeaVMOptimizationLevel.FULL);
        tool.setObfuscated(true);
        TeaBuilder.build(tool);
    }
}
