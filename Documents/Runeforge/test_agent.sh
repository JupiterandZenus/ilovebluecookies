[supervisord]
nodaemon=true
user=root

[program:xvfb]
command=Xvfb :0 -screen 0 1024x768x16
autorestart=true
stdout_logfile=/var/log/xvfb.log
stderr_logfile=/var/log/xvfb.err

[program:x11vnc]
command=x11vnc -display :0 -nopw -forever -shared
autorestart=true
stdout_logfile=/var/log/x11vnc.log
stderr_logfile=/var/log/x11vnc.err

[program:novnc]
command=websockify --web=/usr/share/novnc/ 6081 localhost:5900
autorestart=true
stdout_logfile=/var/log/novnc.log
stderr_logfile=/var/log/novnc.err

[program:flask]
command=python3 /app/gui.py
autorestart=true
stdout_logfile=/var/log/flask.log
stderr_logfile=/var/log/flask.err
