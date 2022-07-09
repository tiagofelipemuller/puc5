#!/usr/bin/env python3

import socket
import sys

HOST = '127.0.0.1'  # localhost = esta máquina

#---------------------------------------------------------
print('Entre com a porta do servidor')
# coloque aqui os comando para receber do usuario a porta do servidor

#---------------------------------------------------------

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    s.bind((HOST, PORT))
except:
   print('# erro de bind')
   sys.exit()

s.listen(5)

# Tratamento de conexão
print('aguardando conexao na porta ', PORT)
conn, addr = s.accept()
print('recebi uma conexao de ', addr)

print('encerrar conexao conexao com', addr)
conn.close()