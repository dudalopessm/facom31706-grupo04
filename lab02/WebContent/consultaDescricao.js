var integrantes = [
    {
        matricula: "12311BCC032",
        nome: "Anderson Gabriel",
        vaga: "Engenheiro de Dados",
        descricao: "Profissional especializado em estruturar pipelines de dados em larga escala. Suas atividades incluem a modelagem de bancos de dados NoSQL e SQL, integração de fontes de dados heterogêneas, otimização de consultas complexas e garantia da integridade e qualidade das informações que sustentam a tomada de decisão da empresa."
    },
    {
        matricula: "12311BCC033",
        nome: "Eduarda Lopes",
        vaga: "Desenvolvedora Frontend",
        descricao: "Especialista em criar interfaces intuitivas e responsivas. Responsável por implementar layouts complexos utilizando HTML, CSS e frameworks JavaScript modernos, garantir a acessibilidade do sistema, otimizar a performance de carregamento das páginas e colaborar diretamente com designers de UI/UX para proporcionar a melhor experiência ao usuário final."
    },
    {
        matricula: "12311BCC019",
        nome: "Gabriel Augusto",
        vaga: "Especialista em Segurança da Informação",
        descricao: "Profissional responsável por proteger a infraestrutura e os dados da organização contra ameaças cibernéticas. Suas atividades envolvem a realização de testes de penetração, monitoramento de vulnerabilidades, implementação de protocolos de criptografia, gestão de identidades e auditoria constante de conformidade com normas de segurança."
    },
    {
        matricula: "12311BCC013",
        nome: "Yan Lucas",
        vaga: "Arquiteto de Soluções Cloud",
        descricao: "Engenheiro focado em desenhar arquiteturas robustas e escaláveis na nuvem. Desenvolve soluções utilizando containers e microsserviços, gerencia a migração de aplicações legadas para ambientes em nuvem, otimiza custos operacionais e garante alta disponibilidade e resiliência dos sistemas críticos da corporação."
    },
    {
        matricula: "12311BCC024",
        nome: "Lucas Matos",
        vaga: "Analista de Qualidade (QA)",
        descricao: "Adestrador de bugs e garantidor da confiabilidade do software. Responsável pelo planejamento e execução de testes automatizados, criação de casos de teste, identificação rigorosa de falhas em novas funcionalidades e suporte contínuo ao time de desenvolvimento para garantir entregas com baixo índice de erros e alta estabilidade."
    },
    {
        matricula: "12411BCC102",
        nome: "Kamily Cristina",
        vaga: "Cientista de Dados",
        descricao: "Arqueóloga de algoritmos e especialista em análise preditiva. Vasculha grandes volumes de dados (Big Data) para identificar padrões ocultos, treina modelos de Machine Learning para prever tendências de negócio, restaura modelos de IA obsoletos e traduz dados complexos em insights estratégicos para o desenvolvimento de novos produtos."
    }
];

function buscarVaga() {
    var matricula = document.getElementById("matricula_vaga").value;
    var tituloVaga = document.getElementById("titulo_vaga");
    var descVaga = document.getElementById("desc_vaga");
    var encontrado = false;

    if (matricula === "") {
        tituloVaga.innerHTML = "Atenção";
        descVaga.innerHTML = "Por favor, digite um número de matrícula antes de pesquisar.";
        return;
    }

    for (var i = 0; i < integrantes.length; i++) {
        if (integrantes[i].matricula === matricula) {
            tituloVaga.innerHTML = integrantes[i].vaga + " — " + integrantes[i].nome;
            descVaga.innerHTML = integrantes[i].descricao;
            encontrado = true;
        } 
        else if (!encontrado && i === integrantes.length - 1) {
            tituloVaga.innerHTML = "Matrícula não encontrada";
            descVaga.innerHTML = "Nenhum integrante do grupo possui a matrícula informada. Verifique o número digitado e tente novamente.";
        }
    }
}