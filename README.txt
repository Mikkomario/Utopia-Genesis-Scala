UTOPIA GENESIS
--------------

v2  ----------------------------------------

New Features
------------

New classes for 2D shapes
    - Size
    - Point
    - Axis & Axis2D

Updates & Changes
-----------------

Major refactoring in shape classes
    - Line & Bounds refactored

Major refactoring in Handler classes
    - Handler classes moved to utopia.genesis.handling
    - ActorHandler refactored & separated to mutable & immutable versions
    - DrawableHandler separated to mutable & immutable versions
    - Same with all MouseEvent and KeyEvent handlers

Actor act(Double) replaced with act(Duration)

ActorThread replaced with ActorLoop

WaitUtil removed (was already deprecated)

KeyStatus class refactored. The constructor is now private.

KeyStateEvent filter methods renamed

MouseButtonStatus class refactored. The constructor is now private.

Mouse Event class changed to trait