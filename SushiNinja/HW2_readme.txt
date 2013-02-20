Readme Hw2:
Name: Angela Yu, Ana Mei
OS: Windows 7
Android 4.2
Eclipse version - 3.8.0, it came with the SDK bundle

The user can draw lines by holding down, moving, and then releasing. It's implemented by 
storing a list of point, and then drawing points and filling in with lines between the points. 

Note: 
Timer doesn't work. It will update in a strange manner if you call invalidate() at the end of onDraw in Palette, but will still let you use PaintBrushView unaffected. We left the 
invalidate() call commented out. 