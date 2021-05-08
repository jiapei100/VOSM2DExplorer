## Getting Started

**VOSM2DExplorer** is a 2D explorer of **Vision Open Statistical Models (VOSM)**. It's used to view [2D statistical models](http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf) widely used for representing nonrighd body transforms. Current updated **VOSM2DExplorer** is of version **1.1** . And, **VOSM2DExplorer** has been licensed under [GPL3](https://www.gnu.org/licenses/gpl-3.0.en.html), and with its own **COPYRIGHT**.


## Environment

After precisely 7 years, do I have the time to have some fun on **VOSM2DExplorer** again.

- OS:
  ```
    ➜  ~ lsb_release -a
    No LSB modules are available.
    Distributor ID:	Ubuntu
    Description:	Ubuntu 20.04.2 LTS
    Release:	20.04
    Codename:	focal
  ```
- JDK: 
  ```
    ➜  ~ java --version
    openjdk 14.0.2 2020-07-14
    OpenJDK Runtime Environment (build 14.0.2+12-Ubuntu-120.04)
    OpenJDK 64-Bit Server VM (build 14.0.2+12-Ubuntu-120.04, mixed mode, sharing)
  ```
- IDE: VSCode 1.56.0


## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies. In fact, the following 5 libraries are depent upon.

  * [swt](https://download.eclipse.org/eclipse/downloads/drops4/R-4.19-202103031800/download.php?dropFile=swt-4.19-gtk-linux-x86_64.zip)
  * [jama](https://math.nist.gov/javanumerics/jama/Jama-1.0.3.jar)
  * [miglayout](http://www.miglayout.com/)
  * [vecmath](https://download.jar-download.com/cache_jars/javax.vecmath/vecmath/1.5.2/jar_files.zip)
  * [jmatharray](https://github.com/yannrichet/jmatharray/blob/master/dist/jmatharray.jar)

    In addition, a modified version of a 3rd party library `nl.skybound.awt.DoublePolygon` is used in **VOSM2DExplorer**, which can be found at <http://www.skybound.nl/products/java/>, and the current version is 1.01 .


## About The Author

Dr. Nobody
Email: <jiapei@longervision.com>
Websites:
- <https://www.longervision.com>
- <https://longervision.github.io>