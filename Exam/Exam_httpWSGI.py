# encoding=utf-8

import socket
import StringIO
import sys, re


class WSGIServer(object):

  address_family = socket.AF_INET
  socket_type = socket.SOCK_STREAM
  request_queue_size = 1

  def __init__(self, server_address):
    # Create a listening socket
    self.listen_socket = listen_socket = socket.socket(
      self.address_family,
      self.socket_type
    )
    # Allow to reuse the same address
    listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # Bind
    listen_socket.bind(server_address)
    # Activate
    listen_socket.listen(self.request_queue_size)
    # Get server host name and port
    host, port = self.listen_socket.getsockname()[:2]
    self.server_name = socket.getfqdn(host)
    self.server_port = port
    # Return headers set by Web framework/Web application
    self.headers_set = []

  def set_app(self, application):
    self.application = application

  def serve_forever(self):
    listen_socket = self.listen_socket
    while True:
      # New client connection
      self.client_connection, client_address = listen_socket.accept()
      # Handle one request and close the client connection. Then
      # loop over to wait for another client connection
      self.handle_one_request()

  def handle_one_request(self):
    self.request_data = request_data = self.client_connection.recv(1024)
    # Print formatted request data a la 'curl -v'
    print(''.join(
      '< {line}\n'.format(line=line)
      for line in request_data.splitlines()
    ))

    self.parse_request(request_data)

    # Construct environment dictionary using request data
    env = self.get_environ()
    print 'env=%r' % env

    # It's time to call our application callable and get
    # back a result that will become HTTP response body
    result = self.application(env, self.start_response)

    # Construct a response and send it back to the client
    self.finish_response(result)

  def parse_request(self, text):
    request_line = text.splitlines()[0]
    request_line = request_line.rstrip('\r\n')
    # Break down the request line into components
    (self.request_method, # GET
     self.path,      # /hello
     self.request_version # HTTP/1.1
     ) = request_line.split()

  def get_environ(self):
    env = {}
    # The following code snippet does not follow PEP8 conventions
    # but it's formatted the way it is for demonstration purposes
    # to emphasize the required variables and their values
    #
    # Required WSGI variables
    env['wsgi.version']   = (1, 0)
    env['wsgi.url_scheme']  = 'http'
    env['wsgi.input']    = StringIO.StringIO(self.request_data)
    env['wsgi.errors']    = sys.stderr
    env['wsgi.multithread'] = False
    env['wsgi.multiprocess'] = False
    env['wsgi.run_once']   = False
    # Required CGI variables
    env['REQUEST_METHOD']  = self.request_method  # GET
    env['PATH_INFO']     = self.path       # /hello
    env['SERVER_NAME']    = self.server_name    # localhost
    env['SERVER_PORT']    = str(self.server_port) # 8888
    return env

  def start_response(self, status, response_headers, exc_info=None):
    # Add necessary server headers
    server_headers = [
      ('Date', 'Tue, 31 Mar 2015 12:54:48 GMT'),
      ('Server', 'WSGIServer 0.2'),
    ]
    self.headers_set = [status, response_headers + server_headers]
    # To adhere to WSGI specification the start_response must return
    # a 'write' callable. We simplicity's sake we'll ignore that detail
    # for now.
    # return self.finish_response

  def finish_response(self, result):
    try:
      status, response_headers = self.headers_set
      response = 'HTTP/1.1 {status}\r\n'.format(status=status)
      for header in response_headers:
        response += '{0}: {1}\r\n'.format(*header)
      response += '\r\n'
      for data in result:
        response += data
      # Print formatted response data a la 'curl -v'
      print(''.join(
        '> {line}\n'.format(line=line)
        for line in response.splitlines()
      ))
      self.client_connection.sendall(response)
    finally:
      self.client_connection.close()


SERVER_ADDRESS = (HOST, PORT) = '', 8888


def make_server(server_address, application):
    server = WSGIServer(server_address)
    server.set_app(application)
    return server


def exam_app(environ, start_response):
    """A barebones WSGI app."""
    response_status = '200 OK'
    response_headers = [('Content-Type', 'text/plain')]
    start_response(response_status, response_headers)
    response_bodys = exam_parseYzxRest(environ)
    return response_bodys   # response_body

# environ['PATH_INFO']="/2014-06-30/Accounts/${sid}${op}?token=${token}${data}"
def exam_parseYzxRest(environ):
    dft_body0 = '{PATH_INFO}={%s}\n' % environ['PATH_INFO']

    # ## 解析 environ['PATH_INFO'] -> infoDc
    infoDc = dict([(i, '') for i in ['pre', 'sid', 'op', 'token', 'data']])
    if not environ['PATH_INFO'].startswith('/2014-06-30/Accounts/'):
        return ['ERROR! MUST: PATH_INFO LIKE /2014-06-30/Accounts%/']
    # 1> %?% -> path?para
    pathinfoSplitPara = environ['PATH_INFO'][len('/2014-06-30/Accounts/'):].split('?')  # 先按?分割
    if len(pathinfoSplitPara) != 2:
        return [dft_body0 + 'ERROR! MUST: PATH_INFO LIKE %?%']
    path, para = pathinfoSplitPara[0], pathinfoSplitPara[1]
    # 2> path: /%/% -> /pre/sid , or /%/%/% -> /pre/sid/op_tail
    pathSplit = path.split('/')
    pathSplitLen = len(pathSplit)
    if (pathSplitLen not in [1, 2]):
        return [dft_body0 + 'ERROR! MUST: PATH_INFO LIKE /%/% or /%/%/%']
    if pathSplitLen == 1:
        infoDc['sid'] = pathSplit[0]
    else:
        infoDc['sid'], op_tail = pathSplit[0], pathSplit[1]
        infoDc['op'] = '/'+op_tail
    # 3> paras: %&% -> token&others , token=${token}${data}
    paraFirstFlagAt = para.find('&')
    if paraFirstFlagAt == -1:
        para_token = para
    else:
        para_token, infoDc['data'] = para[:paraFirstFlagAt], para[paraFirstFlagAt:]
    if not para_token.startswith('token='):
        return [dft_body0 + 'ERROR! MUST: PATH_INFO LIKE %?token=%']
    infoDc['token'] = para_token[len('token='):]
    print "    infoDc=%r" % infoDc

    # ## check infoDc
    if re.match(r'^[a-z\d]{4}$', infoDc['sid']) is None:
        return [u'{"resp":{"respCode":"999999", "respMsg":"{sid}={%s}"}}' % infoDc['sid']]
    if re.match(r'^[A-Z\d]{6}$', infoDc['token']) is None:
        return [u'{"resp":{"respCode":"999999", "respMsg":"{token}={%s}"}}' % infoDc['token']]
    if infoDc['op'] == '':
        print '    @Rest2_GetSrv'
        return [u'{"resp":{"respCode":"000000","account":{"accountSid":"%s","nickName":"nickName"}}}' % infoDc['sid']]
    elif infoDc['op'] == '/Clients':
        print '    @Rest2_GetCliById'
        if not infoDc['data'].startswith('&cliId='):
            return [dft_body0 + 'ERROR! MUST: data LIKE &cliId=%']
        info_cliId = infoDc['data'][len('&cliId='):]
        if re.match(r'^[a-z\d]{7}$', info_cliId) is None:
            return [u'{"resp":{"respCode":"999999", "respMsg":"{cliId}={%s}"}}' % info_cliId]
        return [u'{"resp":{"respCode":"000000","client":{"cliId":"%s"}}}' % info_cliId]
    else:
        return [dft_body0 + 'ERROR! MUST: op in %r' % ['', '/Clients']]


if __name__ == '__main__':
    def if_main():
        pass
    u""" WSGI关键流程:   ==属于WSGI内部==   --不属于WSGI内部--
            http_client     http_server                http_server_wsgi
    step0>                  --serve_forever()监听--
    step1>  发起http请求    --handle_one_request()-->
                            <==response_body=application(environ, start_response)==
                            ==start_response(response_status, response_headers)==>
                            --finish_response()--
    """
    if len(sys.argv) < 2:
        # sys.exit('Provide a WSGI application object as module:callable')
        application = exam_app
    else:
        app_path = sys.argv[1]
        module, application = app_path.split(':')
        module = __import__(module)
        application = getattr(module, application)

    httpd = make_server(SERVER_ADDRESS, application)
    print('WSGIServer: Serving HTTP on port {port} ...\n'.format(port=PORT))
    httpd.serve_forever()

