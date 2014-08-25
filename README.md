EManual-Android  [![GitHub version](https://badge.fury.io/gh/EManual%2FEManual-Client-Java.svg)](http://badge.fury.io/gh/EManual%2FEManual-Client-Java) ![license](http://img.shields.io/badge/license-MIT-brightgreen.svg)
---

**Android client for EManual.**  
EManual makes you learn programming language more  easier.  
Currently, it focuse on **Java and Android**

Configuration
---
### for Eclipse 

1. Java Compiler → Annotation Processing and check `"Enable project specific settings".`

2. Expand the Annotation Processing section and select Factory Path. Check `"Enable project specific settings"` and then click `"Add JARs…"`. Navigate to the project's `libs/` folder and select the Butter Knife jar.

3. Click `"Ok"` to save the new settings. Eclipse will ask you to rebuild your project to which you should click `"Yes"`
Make sure that the `.apt_generated/` folder is in your project root. It should contain files like `YOURACTIVITY$$ViewInjector.java`. If these files are not present trigger a clean build by selected `Project → Clean`. This folder and files should **not** be checked into revision control

4.still error faild? restart the Eclipse 

### for IntelliJ IDEA 

familiar with Eclipse.Click [here](http://jakewharton.github.io/butterknife/ide-idea.html)


License
=======
``` 
Copyright 2014 Jayin Ton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
