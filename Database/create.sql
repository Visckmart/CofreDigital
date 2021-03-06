DROP TABLE IF EXISTS USUARIOS;
CREATE TABLE USUARIOS (
	email varchar(255) PRIMARY KEY,
	senha varchar(2048),
	salt varchar(1024),
	certificado BLOB,
	attempts INTEGER default 0,
	queries INTEGER default 0,
	accesses INTEGER default 0,
	timeout TIMESTAMP,
	gid INTEGER,
	FOREIGN  KEY (gid)
	    REFERENCES GRUPOS(gid)
);

DROP TABLE IF EXISTS GRUPOS;
CREATE TABLE GRUPOS (
	gid INTEGER PRIMARY KEY
);

DROP TABLE IF EXISTS MENSAGENS;
CREATE TABLE MENSAGENS (
	codigo INTEGER PRIMARY KEY,
	mensagem varchar(2048)
);

INSERT INTO MENSAGENS VALUES
(1001, 'Sistema iniciado.'),
(1002, 'Sistema encerrado.'),
(2001, 'Autenticação etapa 1 iniciada.'),
(2002, 'Autenticação etapa 1 encerrada.'),
(2003, 'Login name <login_name> identificado com acesso liberado.'),
(2004, 'Login name <login_name> identificado com acesso bloqueado.'),
(2005, 'Login name <login_name> não identificado.'),
(3001, 'Autenticação etapa 2 iniciada para <login_name>.'),
(3002, 'Autenticação etapa 2 encerrada para <login_name>.'),
(3003, 'Senha pessoal verificada positivamente para <login_name>.'),
(3004, 'Primeiro erro da senha pessoal contabilizado para <login_name>.'),
(3005, 'Segundo erro da senha pessoal contabilizado para <login_name>.'),
(3006, 'Terceiro erro da senha pessoal contabilizado para <login_name>.'),
(3007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 2.'),
(4001, 'Autenticação etapa 3 iniciada para <login_name>.'),
(4002, 'Autenticação etapa 3 encerrada para <login_name>.'),
(4003, 'Chave privada verificada positivamente para <login_name>.'),
(4004, 'Chave privada verificada negativamente para <login_name> (caminho inválido).'),
(4005, 'Chave privada verificada negativamente para <login_name> (frase secreta inválida).'),
(4006, 'Chave privada verificada negativamente para <login_name> (assinatura digital inválida).'),
(4007, 'Acesso do usuario <login_name> bloqueado pela autenticação etapa 3.'),
(5001, 'Tela principal apresentada para <login_name>.'),
(5002, 'Opção 1 do menu principal selecionada por <login_name>.'),
(5003, 'Opção 2 do menu principal selecionada por <login_name>.'),
(5004, 'Opção 3 do menu principal selecionada por <login_name>.'),
(5005, 'Opção 4 do menu principal selecionada por <login_name>.'),
(6001, 'Tela de cadastro apresentada para <login_name>.'),
(6002, 'Botão cadastrar pressionado por <login_name>.'),
(6003, 'Senha pessoal inválida fornecida por <login_name>.'),
(6004, 'Caminho do certificado digital inválido fornecido por <login_name>.'),
(6005, 'Confirmação de dados aceita por <login_name>.'),
(6006, 'Confirmação de dados rejeitada por <login_name>.'),
(6007, 'Botão voltar de cadastro para o menu principal pressionado por <login_name>.'),
(7001, 'Tela de alteração da senha pessoal e certificado apresentada para <login_name>.'),
(7002, 'Senha pessoal inválida fornecida por <login_name>.'),
(7003, 'Caminho do certificado digital inválido fornecido por <login_name>.'),
(7004, 'Confirmação de dados aceita por <login_name>.'),
(7005, 'Confirmação de dados rejeitada por <login_name>.'),
(7006, 'Botão voltar de carregamento para o menu principal pressionado por <login_name>.'),
(8001, 'Tela de consulta de arquivos secretos apresentada para <login_name>.'),
(8002, 'Botão voltar de consulta para o menu principal pressionado por <login_name>.'),
(8003, 'Botão Listar de consulta pressionado por <login_name>.'),
(8004, 'Caminho de pasta inválido fornecido por <login_name>.'),
(8005, 'Arquivo de índice decriptado com sucesso para <login_name>.'),
(8006, 'Arquivo de índice verificado (integridade e autenticidade), com sucesso para <login_name>.'),
(8007, 'Falha na decriptação do arquivo de índice para <login_name>.'),
(8008, 'Falha na verificação (integridade e autenticidade), do arquivo de índice para <login_name>.'),
(8009, 'Lista de arquivos presentes no índice apresentada para <login_name>.'),
(8010, 'Arquivo <arq_name> selecionado por <login_name> para decriptação.'),
(8011, 'Acesso permitido ao arquivo <arq_name> para <login_name>.'),
(8012, 'Acesso negado ao arquivo <arq_name> para <login_name>.'),
(8013, 'Arquivo <arq_name> decriptado com sucesso para <login_name>.'),
(8014, 'Arquivo <arq_name> verificado (integridade e autenticidade), com sucesso para <login_name>.'),
(8015, 'Falha na decriptação do arquivo <arq_name> para <login_name>.'),
(8016, 'Falha na verificação (integridade e autenticidade), do arquivo <arq_name> para <login_name>.'),
(9001, 'Tela de saída apresentada para <login_name>.'),
(9002, 'Saída não liberada por falta de one-time password para <login_name>.'),
(9003, 'Botão sair pressionado por <login_name>.'),
(9004, 'Botão voltar de sair para o menu principal pressionado por <login_name>.');

DROP TABLE IF EXISTS REGISTROS;
CREATE TABLE REGISTROS (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
	codigo INTEGER,
	usuario varchar(255),
	arquivo varchar(255),
	FOREIGN KEY (usuario)
		REFERENCES USUARIO (email),
	FOREIGN KEY (codigo)
		REFERENCES MENSAGENS (codigo)
);
