# Java Raytracer - City Smith

Recently created raytracer written in Java that renders 3D scenes defined as methods in the code. It started as a class assignment and became a two-month personal project after my teacher went on leave. Made at age 16 from Apr-May of 2026; Submitted for MIT Maker Portfolio.

## WHAT IT DOES

- Casts a ray through every pixel and finds the closest surface it hits
- Stores and combines colors
- Shadows
- Diffuse shading (Lambert) and specular highlights (Phong)
- Mirror reflections (MirrorPhong), up to _n_ (n is 3 in the code) bounces
- Anti-aliasing by supersampling
- Parallel processing: image columns are split across all CPU cores
- OBJ parser that loads `.obj` files as triangle meshes (quads are split into triangles)
- Animation over a series of frames, exported to MP4 with JCodec

## HOW IT WORKS

1. `RaytracerDriver` builds a scene from `SceneCreator` and calls `Scene.render`
2. For each pixel, `render` shoots an `aaRes x aaRes` grid of sample rays and averages the colors
3. `computeVisibleColor` finds the closest hit, adds light from each unshadowed light, then recursively follows a reflected ray if the material is reflective
4. Every ray is tested against every surface; scenes with many triangles render slowly

## HOW TO RUN

1. Download `jcodec-0.2.5.jar` and `jcodec-javase-0.2.5.jar` (JCodec link in Credits below).
2. In BlueJ, go to Tools → Preferences → Libraries → Add File. Select each jar and click OK.
3. Restart BlueJ and open this project.
4. Right-click `RaytracerDriver` and run `main` (set to render the car spinning video by default).
5. Change resolution and the number of frames at the top of the driver. Resolution affects runtime a lot.

## CREDITS AND SOURCES

### Starter Code
Farrar, B. (2019). *Raytracer starter files* [Source code]. Provided by instructor.
Includes the Sphere, Triangle, Cone, Tube, and Ring surface classes, a sample
RaytracerDriver (main method plus image-saving methods), and SceneCreator's scene1.
Everything else in this repository is my own code.

### Libraries
JCodec. (n.d.). *JCodec* [Computer software]. GitHub. https://github.com/jcodec/jcodec
(Used for MP4 export. I heard about it from my classmate Gregory Povetko.)

### 3D Models (Free3D, Personal Use License)
3dhaupt. (n.d.). *Indoor pot plant 2* [3D model]. Free3D. (NOT IN REPO OR IN ANY SCENES)
https://free3d.com/3d-model/indoor-pot-plant-77983.html

printable_models. (n.d.). *WWII soldier throwing grenade V2* [3D model]. Free3D.
https://free3d.com/3d-model/wwii-soldier-throwing-grenade-v2--322379.html

wufudufu. (n.d.). *Low-poly car* [3D model]. Free3D.
https://free3d.com/3d-model/low-poly-car-40967.html

### Documentation and Tutorials
GeeksforGeeks. (n.d.). *Difference between BufferedReader and FileReader in Java*.
https://www.geeksforgeeks.org/java/difference-between-bufferedreader-and-filereader-in-java/

GeeksforGeeks. (n.d.). *IntStream parallel() in Java*.
https://www.geeksforgeeks.org/java/intstream-parallel-java/

Oracle. (n.d.). *BufferedReader* (Java Platform SE 8) [Documentation].
https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html

Dr. Pavel Vlašánek. (2020). *OBJ to ASCII: Render 3D Models in Terminal with C++* [Video]. YouTube. https://www.youtube.com/watch?v=YwBHlo_T8Ao

The Coding Train. (2018). *18.7: Loading OBJ Model - WebGL and p5.js Tutorial* [Video]. YouTube. https://youtu.be/FUI7HEEz9B0

Will Tollefson. (2023). *Are You Using Java's Parallel Streams Correctly? - Java Programming* [Video]. YouTube. https://www.youtube.com/watch?v=j2kp4d_axhI

Keep On Coding. (2020). *Java Streams Tutorial* [Video]. YouTube. https://www.youtube.com/watch?v=FWoYpM-E3EQ

(OBJ parsing and parallel streams)

mcjabberz. (2008). *How to split a string with any whitespace chars as delimiters* [Online forum post]. Stack Overflow.
https://stackoverflow.com/questions/225337

mpluse. (2013). *Regex expressions in Java, \\s vs. \\s+* [Online forum post]. Stack Overflow.
https://stackoverflow.com/questions/15625629

Also used: BlueJ documentation and TutorialsPoint (specific pages not recorded).

### Assistive Tools
Anthropic. (2026). *Claude* [Large Language Model]. https://claude.ai

I used Claude to ask whether Java had a class or method for something I already wanted to do using prior knowledge from other IDEs. I also made it explain unfamiliar methods when the documentation was confusing. I wrote the code myself.
It also helped write this README, I'm new to GitHub.