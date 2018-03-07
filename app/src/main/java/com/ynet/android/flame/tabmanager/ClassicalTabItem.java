package com.ynet.android.flame.tabmanager;

/**
 * <p>ClassicalTabItem：TabManager经典样式下的数据类，用于Flame开发平台的首页底部（经典样式）管理的数据源管理，在Flame开发平台中使用。<br>
 * TabManager经典样式在本包中标识，上方显示图片，下方显示文字的样式。<br>
 * 各位小伙伴在项目实施过程中，请反馈其中的不足，我们共同改进。
 * <p>
 * = * @version v1.0.0
 */
public class ClassicalTabItem extends TabItem {
    private String name;
    private String icon;
    private String selectedIcon;
    private String color;
    private String selectedColor;
    private String textColor;
    private String textSelectedColor;
    private String tags;

    /**
     * 获取name属性，name属性决定文字的显示内容，对应config.xml中<Tab>的name属性
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性，name属性决定文字的显示内容，对应config.xml中<Tab>的name属性
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取icon属性，icon属性决定图片默认的显示内容，
     * 对应config.xml中<Tab>的icon属性，
     * 使用时，将图片置放在res/drawable目录中，对应属性填写对应的文件名（不包含扩展名）即可。
     *
     * @return icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置icon属性，icon属性决定图片默认的显示内容，
     * 对应config.xml中<Tab>的icon属性，
     * 使用时，将图片置放在res/drawable目录中，对应属性填写对应的文件名（不包含扩展名）即可。
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取color属性，color属性决定TabItem的View的默认背景色，
     * 对应config.xml中<Tab>的color属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置color属性，color属性决定TabItem的View的默认背景色，
     * 对应config.xml中<Tab>的color属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 获取textColor属性，textColor属性决定显示文字的默认字体颜色，
     * 对应config.xml中<Tab>的textColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @return textColor
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * 设置textColor属性，textColor属性决定显示文字的默认字体颜色，
     * 对应config.xml中<Tab>的textColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @param textColor
     */
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * 获取selectedIcon属性，selectedIcon属性决定TabItem的View的选中时图片的显示内容，
     * 对应config.xml中<Tab>的selectedIcon属性，
     * 使用时，将图片置放在res/drawable目录中，对应属性填写对应的文件名（不包含扩展名）即可。
     *
     * @return selectedIcon
     */
    public String getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * 设置selectedIcon属性，selectedIcon属性决定TabItem的View的选中时图片的显示内容，
     * 对应config.xml中<Tab>的selectedIcon属性，
     * 使用时，将图片置放在res/drawable目录中，对应属性填写对应的文件名（不包含扩展名）即可。
     *
     * @param selectedIcon
     */
    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    /**
     * 获取selectedColor属性，selectedColor属性决定TabItem的View的选中时的背景色，
     * 对应config.xml中<Tab>的selectedColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @return selectedColor
     */
    public String getSelectedColor() {
        return selectedColor;
    }

    /**
     * 设置selectedColor属性，selectedColor属性决定TabItem的View的选中时的背景色，
     * 对应config.xml中<Tab>的selectedColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @param selectedColor
     */
    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * 获取textSelectedColor属性，textSelectedColor属性决定TabItem的View的选中时显示文字的字体颜色，
     * 对应config.xml中<Tab>的textSelectedColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @return textSelectedColor
     */
    public String getTextSelectedColor() {
        return textSelectedColor;
    }

    /**
     * 设置textSelectedColor属性，textSelectedColor属性决定TabItem的View的选中时显示文字的字体颜色，
     * 对应config.xml中<Tab>的textSelectedColor属性，
     * 使用时，可以使用"#FFFFFF"的形式设置，也可以使用资源名(如："myColor")设置，
     * 但使用资源名设置时，需本包内包含该资源名对应的资源(如：R.color.myColor)。
     *
     * @param textSelectedColor
     */
    public void setTextSelectedColor(String textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }

    /**
     * 获取tags属性，tags属性目前未使用，作为扩展字段预留，
     * 对应config.xml中<Tab>的tags属性，
     *
     * @return tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置tags属性，tags属性目前未使用，作为扩展字段预留，
     * 对应config.xml中<Tab>的tags属性，
     *
     * @param tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

}
