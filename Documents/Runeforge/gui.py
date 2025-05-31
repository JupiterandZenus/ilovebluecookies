if __name__ == '__main__':
    import os
    host = os.getenv('HOST', '192.168.1.104')
    port = int(os.getenv('PORT', 6079))
    app.run(host=host, port=port)
