<h1 align="center">Pdf Viewer For Android</h1>

<p align="center">
A Simple PDF Viewer library which renders images via <a href="https://developer.android.com/reference/android/graphics/pdf/PdfRenderer.html">android.graphics.pdf.PdfRenderer</a>
<br>
<br>
<img src="https://raw.githubusercontent.com/cboyce428/Pdf-Viewer/master/Screenshot_2020-07-11-23-59-31-606_com.rajat.pdfviewer.jpg" width="420" height="840" />
</p>

[![](https://jitpack.io/v/afreakyelf/Pdf-Viewer.svg)](https://jitpack.io/#afreakyelf/Pdf-Viewer) [![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/Apache-2.0) ![](https://img.shields.io/github/forks/afreakyelf/Pdf-Viewer?label=Forks)
![](https://img.shields.io/github/stars/afreakyelf/Pdf-Viewer?label=Stars&color=9cf) ![](https://visitor-badge.glitch.me/badge?page_id=afreakyelf.Pdf-Viewer)[![](https://jitci.com/gh/afreakyelf/Pdf-Viewer/svg)](https://jitci.com/gh/afreakyelf/Pdf-Viewer)

## Why is this forked
1. To remove excess permissions
2. Removed download ability calling apps should be responsible for this & any cleanup
3. Removed page numbers as did not work properly in landscape
4. Update dependencies to latest versions & code changes due to removal of kotlin-android-extensions

## How to integrate into your app?
Integrating the project is simple, All you need to do is follow the below steps

Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```java
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```

Step 2. Add the dependency
```java
dependencies {
    implementation 'com.github.cboyce428:Pdf-Viewer:v{Tag}'
}
```
NOTE: Replace the tag with current release version, e.g

```java
implementation 'com.github.cboyce428:Pdf-Viewer:v1.1.1b'
```

## How to use the library?
Now you have integrated the library in your project but **how do you use it**? Well its really easy just launch the intent with in following way:

### Kotlin
```kotlin
open_pdf.setOnClickListener {
            startActivity(

            // Opening pdf from file storage
                PdfViewerActivity.Companion.launchPdfFromPath(
                    context,
                    File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "yourfile.pdf").absolutePath,
                    "PDF from File",
                    false
                )
            )
        } 
```

### Java

```java
        open_pdf.setOnClickListener(view -> {
            startActivity(
            
            // Opening pdf from assets folder
            
                    PdfViewerActivity.Companion.launchPdfFromPath(
                            this,
                            "asst_name.pdf",
                            "PDF from Asset,
                            true
                    )
            );
        });

```

That's pretty much it and you're all wrapped up.

### Ui Customizations
You need to add the custom theme to styles.xml/themes.xml file and override the required attribute values.
Parent theme can be either **Theme.PdfView.Light** or **Theme.PdfView.Dark** or the one with no actionbar from the application.
Note: If parent is not one of the themes from this library, all of the pdfView attributes should be added to that theme.

    <style name="Theme.PdfView.SelectedTheme" parent="@style/Theme.PdfView.Light">
        <item name="pdfView_backIcon">@drawable/ic_arrow_back</item>
        <item name="pdfView_actionBarTint">@color/red</item>
        <item name="pdfView_titleTextStyle">@style/pdfView_titleTextAppearance</item>
        <item name="pdfView_progressBar">@style/pdfView_progressBarStyle</item>
    </style>

    <style name="Theme.PdfView.SelectedTheme" parent="@style/Theme.PdfView.Dark">
        <item name="pdfView_backIcon">@drawable/ic_arrow_back</item>
        <item name="pdfView_actionBarTint">@color/black</item>
        <item name="pdfView_titleTextStyle">@style/pdfView_titleTextAppearance</item>
        <item name="pdfView_progressBar">@style/pdfView_progressBarStyle</item>
    </style>	


#### Supported attributes

| Attribute Name | Type | Expected changes |
|--|--|--|
|pdfView_backIcon|drawable|Navigation icon|
|pdfView_actionBarTint|color|Actionbar background color|
|pdfView_titleTextStyle|style|Actionbar title text appearance|
|pdfView_progressBar|style|Progress bar style|

## Contributing

Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/NewFeature`)
3. Commit your Changes (`git commit -m 'Add some NewFeature'`)
4. Push to the Branch (`git push origin feature/NewFeature`)
5. Open a Pull Request

## Donations
If this project help you reduce time to develop, you can give me a cup of coffee :)

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/paypalme/afreakyelf)

## Author
Maintained by [Rajat Mittal](https://www.github.com/afreakyelf)
