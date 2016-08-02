# wso2-esb-pdf-mediator
![Build status](https://circleci.com/gh/Mystes/wso2-esb-pdf-mediator.svg?style=shield&circle-token=8e9634318a30032cfb0db87a030a65a97380bcb5)
## What is WSO2 ESB?
[WSO2 ESB](http://wso2.com/products/enterprise-service-bus/) is an open source Enterprise Service Bus that enables interoperability among various heterogeneous systems and business applications.

## Features
PDF Mediator is a custom WSO2 ESB mediator for exporting payload to PDF file. First mediator converts XML payload into HTML (document type HTML 5) using given XSL and CSS file and then it exports the result to PDF file.

## Usage

### 1. Install the mediator
Copy the `wso2-esb-pdf-mediator-x.y.jar` to `$WSO2_ESB_HOME/repository/components/dropins/`.

### 2. Use it
Mediator can be used as other WSO2 ESB mediator.
```xml
<pdf>
  <pdfFilePath (value="literal" | expression="xpath")/>
  <cssFilePath (value="literal" | expression="xpath")/>
  <xslFilePath (value="literal" | expression="xpath")/>
</pdf>
```

#### Example
```xml
<pdf>
  <pdfFilePath expression="concat('/path/to/save/to/dir/',$ctx:timestamp,'_export.pdf')"/>
  <cssFilePath value="/path/to/file.css"/>
  <xslFilePath value="/path/to/file.xsl"/>
</pdf>
```

## Technical Requirements

#### Usage

* Oracle Java 6 or higher
* WSO2 ESB
    * Pdf Mediator has been tested with WSO2 ESB versions 4.8.1

#### Development

* All above + Maven 3.0.X

### Contributors

- [Kreshnik Gunga](https://github.com/kgunga)
- [Heikki Häyhä](https://github.com/heikkihay)
- [Ville Harvala](https://github.com/vharvala)

## [License](LICENSE)

Copyright &copy; 2016 [Mystes Oy](http://www.mystes.fi). Licensed under the [Apache 2.0 License](LICENSE).
