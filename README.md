# Docx Converter for Legacy Ethiopic Font Encoding Systems


## About

This repository may be imported into [Eclipse](http://www.eclipse.org) as a simple [Maven](https://maven.apache.org/) Java project.
The "Eclipse IDE for Java Developers" download option will include both Maven and Git integration, no additional installation will
be needed to build this project.  Maven in turn will retrieve all dependencies, primarily the [docx4j](https://www.docx4java.org/)
(6.0.1) and [ICU](http://site.icu-project.org/) (63.1) and Java libaries.

The converter presently provides support for two legacy (non-Unicode) systems, Brana (I &amp; II) and Feedel Ge'ez (New &amp; B).
The conversion mappings come directly from the long defunct [LibEth](http://libeth.sourceforge.net) C language library which
supported conversion of many more [legacy encoding systems](http://libeth.sourceforge.net/CharacterSets.html).  Support for additional
encodings systems can be porrted from LibEth as the need arrises. Please feel free to request support for additional systems.


## Usage Examples

% java -cp [path to libaries]  &lt;system&gt; &lt;file-in&gt; &lt;file-out&gt;

&lt;system&gt; is either:  **brana** or **geeznewab**

### Brana 90

The Brana 90 encoding system did _not_ decompose letters into base forms and separate diacritical marks. Instead, it split
the full syllabary across two two fonts, Brana I and Brana II respectively. Brana 90 was a proprietary application that
used the HighEdit document format as its native system.

1. In the Brana 90 application, save a document as RTF.
2. Open the RTF document in Microsoft WordPad and Save As... Office Open XML Document (.docx)  
   (MS Word 2016 is unable to open RTF documets saved from Brana 90).
3. The converter application may be run simply from Eclipse. At the command line, you will need to specify the paths
   to depending librariesl in a form similar to:

```
% java -cp DocxConverter-0.0.1-SNAPSHOT.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx brana MyFileIn.docx MyFileOut.docx 
```


### Feedel

The Feedel company produced three encoding systems that all took the approach of decomposing letters into base forms and
separate diacritical marks. The most widely used of the three encoding systems used two fonts: GeezNewA and GeezNewB. The
Feedel application was a keyboard utility that could be used in Microsoft Windows systems up until Windows XP.  The
following steps assume that Feedel documents were composed in older versions of Microsoft Word:

1. Open a Feedel .doc file in a recent version of Microsoft Word (2007 or later).
2. Save the document from Word as a Word Document (.docx)
3. The converter application may be run simply from Eclipse. At the command line, you will need to specify the paths
   to depending librariesl in a form similar to:

```
% java -cp DocxConverter-0.0.1-SNAPSHOT.jar:docx4j-6.0.1.jar:dependencies/*:icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab MyFileIn.docx MyFileOut.docx 
```

