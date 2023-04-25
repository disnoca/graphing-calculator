# Graphing Calculator
A graphing calculator based on Casio's physical graphing calculator fx-CG50 with the same settings and mathematical G-Solve functions.
These include:
- Root
- Maximum
- Minimum
- Intersection with the Y-Axis
- Intersection between two functions
- Y-Value
- X-Value
- Integral

## Aditional Features
The current workspace can be saved and loaded

The current screen's contents can be saved into a png or jpg file

The window can be resized at will and the referential contents will correctly scale along

The referential can be zoomed in and out using scroll and moved by dragging with the mouse


## Build Project

To build this project, a library called exp4j is needed. The library is available here: https://www.objecthunter.net/exp4j/download.html

After downloading the library, you'll need to add it to your project's build path.

## Current Known Issues
Functions that have no solutions on certain points and tend towards positive and negative infinities on those points' limits will be drawn wrongly with vertical lines in those points. The G-Solve functions can produce wrong results when used on these functions.

If the referential is zoomed out too much the program will freeze.

As a byproduct of these two issues, using the G-Solve Intersection function on the above mentioned functions may cause the program to crash.

## Disclaimer
This project was done in a time where I didn't care much about comments or documentation and was "finished" in a rush so the code is kind of a mess and very difficult to understand unless you dive deep into it. I apologise in advance if anyone wants to use it or learn from it. I might refactor it in the future if I feel like it.
