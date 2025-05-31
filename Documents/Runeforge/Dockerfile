FROM ubuntu:22.04

# Install dependencies
RUN apt-get update && apt-get install -y \
    python3 python3-pip supervisor xvfb x11vnc novnc websockify tigervnc-standalone-server \
    nginx curl net-tools procps vim \
    && rm -rf /var/lib/apt/lists/*

# Copy app files
COPY . /app
WORKDIR /app

# Install python dependencies if needed
RUN pip3 install flask

# Make scripts executable
RUN chmod +x account_manager.sh cpu_manager.sh start.sh

# Expose ports
EXPOSE 5900 6081 6079 5000

# Start supervisord to run all services
CMD ["/app/start.sh"]
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf
