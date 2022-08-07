# DigImgProcess
This Java project was used to process digital microscopy images that had been manually annotated. 

The annotated images assumed specific preparation methodology for compatibility:
-Raw microscopy images are in grayscale
-Microscopy images are square 
-Annotations are in color
-Annotations do not overlap
-Annotations of the same color are not nested, at any level of nesting
-Annotations are 1 pixel wide
-Annotations create closed shapes (pixel contiguity for annotations is determined on the diagonal and cardinal directions)

In practice, annotation was performed by importing the raw microscopy raster images into an SVG editor, drawing polygons using the editor (resulting in an intermediate stage that is partially SVG and partially rasterized), then using a browser-based SVG renderer to retrieve a fully rasterized image. 
*Note: Testing of browser renderers was done only between Google Chrome and Mozilla Firefox with hardware acceleration enabled (both at the latest version as of March 2022). While Chrome had better render performance than Firefox, Chrome also unavoidably adjusted color data unlike Firefox. Firefox is recommended should the aforementioned annotation procedure be replicated.*

Code in this project is not built to function as a conventional monolithic application, instead multiple individual classes contain main methods and were ran individually as needed.

## Key Classes

### src/main/java/Floodmaker.java
Contains an implementation of a recursive 4-way floodfill. Given an image and the coordinates of an arbitrary pixel in that image, all grayscale pixels contiguous with the seed pixel are marked with the color of the deepest nested annotation they are within.

### src/main/java/BoundaryAnalyzer.java
Contains an implementation of the ray-casting algorithm to determine whether a given pixel is inside a polygon/annotation, as well as identifying the color of the polygon it is nested deepest within. Due to the inherent limitation of detecting vertices when using this approach in the context of rasterized images (as opposed to vectorized), annoations are required to be 1 pixel wide, as thin as possible.

### src/main/DotAnalyzer.java
Invokes the methods in the above 2 classes (and others) to execute almost all other essential functions. Contains additional logic to:
-Calculate the real-scale dimensional equivalent for a pixel using scale bars embedded onto the raw image
-Generate HTML files summarizing color coverage for images
-Debugging methods for outputting painted images (useful for identifying floodfill failures due to "broken" polygons which had discontinuities in their rendering)

## Miscellaneous
The src/main/test package contains test classes that are not true JUnit tests. They function similarly, they are meant to be ran normally via main methods, but expect their output to be manually confirmed for correctness. 