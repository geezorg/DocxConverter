# Command Line Usage Examples

% java -cp [path to libaries]  &lt;system&gt; &lt;file-in&gt; &lt;file-out&gt;

&lt;system&gt; is one of:  **brana** , **geeznewab** , **geeztypenet** , **powergeez** , or **visualgeez** .

## Brana 90
**Fonts Supported**
* Brana I
* Brana II

The Brana 90 encoding system did _not_ decompose letters into base forms and separate diacritical marks. Instead, it split
the full syllabary across two two fonts, Brana I and Brana II respectively. Brana 90 was a proprietary application that
used the HighEdit document format as its native system.

1. In the Brana 90 application, save a document as RTF.
2. Open the RTF document in Microsoft WordPad and Save As... Office Open XML Document (.docx)  
   (MS Word 2016 is unable to open RTF documets saved from Brana 90).
3.  At the command line, you will need to specify the paths to depending librariesl in a form similar to:

```
% java -cp DocxConverter-0.5.0.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx brana MyFileIn.docx MyFileOut.docx 
```


## Feedel

**Fonts Supported**
* GeezA, GeezB
* GeezNet
* GeezNewA, GeezNewB
* GeezSindeA, GeezSindeB
* ZewdituA, ZewdituB

The Feedel company produced three encoding systems that all took the approach of decomposing letters into base forms and
separate diacritical marks. The most widely used of the three encoding systems used two fonts: GeezNewA and GeezNewB. 
Ethiopic numerals and a few additional letters were housed in the secondary file.  The Feedel application was a keyboard
utility that could be used in Microsoft Windows systems up until Windows XP.

The following steps assume that Feedel documents were composed in older versions of Microsoft Word:

1. Open a Feedel .doc file in a recent version of Microsoft Word (2007 or later).
2. Save the document from Word as a Word Document (.docx)
3.  At the command line, you will need to specify the paths to dependent libraries in a form similar to:

```
% java -cp DocxConverter-0.5.0.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab MyFileIn.docx MyFileOut.docx 
```


## Ge'ezSoft

**Fonts Supported**
* GeezTypeNet

The GeezSoft company produced two encoding systems that took the approach of decomposing letters into base forms and
separate diacritical marks. The two systems each contained the syllabary to a single font. The most widely used of the two
encoding systems, GeezTypeNet, is supported.  The older system, GeezType, is not yet supported.

The following steps assume that Feedel documents were composed in older versions of Microsoft Word:

1. Open a Ge'ezSoft .doc file in a recent version of Microsoft Word (2007 or later).
2. Save the document from Word as a Word Document (.docx)
3. At the command line, you will need to specify the paths to dependent libraries in a form similar to:

```
% java -cp DocxConverter-0.5.0.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeztypenet MyFileIn.docx MyFileOut.docx 
```


## Power Ge'ez

**Fonts Supported**
* Ge'ez-1
* Ge'ez-2
* Ge'ez-3
* Ge'ez-1 Numbers

The Concepts Data Systems, PLC company produced a single encoding systems that approach of decomposing letters into base forms and
separate diacritical marks.  Ethiopic numerals and a few additional letters were housed in the secondary Ge'ez-1 Numbers font.
The Power Ge'ez application was a keyboard Feedel application was a keyboard utility that could be used in Microsoft Windows.

The following steps assume that Power Ge'ez documents were composed in older versions of Microsoft Word:

1. Open a Power Ge'ez .doc file in a recent version of Microsoft Word (2007 or later).
2. Save the document from Word as a Word Document (.docx)
3. At the command line, you will need to specify the paths to dependent libraries in a form similar to:

```
% java -cp DocxConverter-0.5.0.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx powergeez MyFileIn.docx MyFileOut.docx 
```



## Visual Ge'ez

**Fonts Supported**
* VG2 Main
* VG2 Agazian
* VG2 Title
* VG Geez Numbers

The CUSTCOR company produced three encoding systems that all took the approach of decomposing letters into base forms and
separate diacritical marks.  Ethiopic numerals and a few additional letters were housed in the secondary VG Geez Numbers font.
The Visual Ge'ez application was a keyboard utility that could be used in Microsoft Windows.

The following steps assume that Visual Ge'ez documents were composed in older versions of Microsoft Word:

1. Open a Visual Ge'ez .doc file in a recent version of Microsoft Word (2007 or later).
2. Save the document from Word as a Word Document (.docx)
3. At the command line, you will need to specify the paths to dependent libraries in a form similar to:

```
% java -cp DocxConverter-0.5.0.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx visualgeez MyFileIn.docx MyFileOut.docx 
```
