#!/bin/bash

# Start supervisord (if you use supervisor)
# Or start processes manually here

# Start xvfb (virtual frame buffer for GUI)
Xvfb :0 -screen 0 1024x768x16 &

# Start window manager, desktop environment, VNC server
x11vnc -display :0 -nopw -forever -shared &

# Start noVNC web client on port 6081
websockify --web=/usr/share/novnc/ 6081 localhost:5900 &

# Start Flask app (GUI)
python3 gui.py &

# Start supervisord
exec supervisord -c /etc/supervisor/conf.d/supervisord.conf

# Wait for all processes to finish
wait
