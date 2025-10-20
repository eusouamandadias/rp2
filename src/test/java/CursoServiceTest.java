import model.Curso;
import model.StatusCurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CursoService;
import service.JsonDataManager;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class CursoServiceTest {

    private JsonDataManager dataManager;
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        this.dataManager = new JsonDataManager();
        this.cursoService = new CursoService(dataManager);
    }

    // REQUISITO: Criar novos cursos (Professor)
    @Test
    void criarCursoDeveAdicionarNovoCursoAPersistencia() {
        // TODO: Testar a criação de um novo curso:
        int tamanhoLista = dataManager.getCursos().size();

        String titulo = "RP II - Testes Unitários";
        String descricao = "Aprendendo TU";
        String professorId = "p1";
        Curso novoCurso = cursoService.criarCurso(titulo, descricao, professorId);

        // 1. Verificar se o novo curso não é nulo.
        assertNotNull(novoCurso, "O curso criado não pode ser nulo.");

        // 2. Verificar se o título e a descrição estão corretos.
        assertEquals(titulo, novoCurso.getTitulo(), "O título está correto.");
        assertEquals(descricao, novoCurso.getDescricao(), "A descrição está correta.");

        // 3. Confirmar que o status inicial é PENDENTE_APROVACAO.
        assertEquals(StatusCurso.PENDENTE_APROVACAO, novoCurso.getStatus());

        // 4. Garantir que o tamanho da lista de cursos aumentou em 1.
        assertEquals(tamanhoLista + 1, dataManager.getCursos().size(), "A lista de cursos deve aumentar em 1.");
    }

    // REQUISITO: Editar cursos existentes (Professor/Admin)
    @Test
    void editarCursoDeveAtualizarTituloEDescricao() {
        // TODO: Testar a edição de um curso existente (ex: "c1"):
        String cursoId = "c1";
        String novoTitulo = "Curso de Testes Unitários(TU)";
        String novaDescricao = "Como aprender a realizar TU";
        
        // 1. Chamar editarCurso() e verificar se o retorno é 'true'.
        boolean resultado = cursoService.editarCurso(cursoId, novoTitulo, novaDescricao);
        assertTrue(resultado, "O curso foi editado.");

        // 2. Recuperar o curso na persistência (dataManager).
        Optional<Curso> cursoEditado = dataManager.getCursos().stream()
                .filter(c -> c.getId().equals(cursoId))
                .findFirst();
        assertTrue(cursoEditado.isPresent(), "O curso editado deve ser encontrado na persistência.");

        // 3. Verificar se o título e a descrição foram atualizados corretamente.
        assertEquals(novoTitulo, cursoEditado.get().getTitulo(), "O título foi atualizado corretamente.");
        assertEquals(novaDescricao, cursoEditado.get().getDescricao(), "A descrição foi atualizada corretamente.");
    }

    // REQUISITO: Configurar proteção por PIN de acesso (Professor)
    @Test
    void configurarPinDeveAdicionarPinAoCurso() {
        // TODO: Testar a configuração de PIN em um curso (ex: "c1"):
        String cursoId = "c1";
        String pin = "1234";
        
        // 1. Chamar configurarPin() e verificar se o retorno é 'true'.
        boolean resultado = cursoService.configurarPin(cursoId, pin);
        assertTrue(resultado, "O PIN foi configurado.");

        // 2. Recuperar o curso na persistência (dataManager).
        Optional<Curso> cursoComPin = dataManager.getCursos().stream()
                .filter(c -> c.getId().equals(cursoId))
                .findFirst();
        assertTrue(cursoComPin.isPresent(), "O curso com pin deve ser encontrado na persistência.");

        // 3. Verificar se o PIN foi adicionado/configurado corretamente.
        assertEquals(pin, cursoComPin.get().getPinAcesso(), "O PIN foi adicionado corretamente.");
    }

    // REQUISITO: Aprovar/rejeitar cursos (Administrador)
    @Test
    void aprovarCursoDeveMudarStatusParaAtivo() {
        // TODO: Testar a aprovação de um curso PENDENTE (ex: "c2"):
        String cursoId = "c2";
        
        // 1. Chamar aprovarCurso() e verificar se o retorno é 'true'.
        boolean resultado = cursoService.aprovarCurso(cursoId);
        assertTrue(resultado, "O curso foi aprovado.");

        // 2. Recuperar o curso na persistência (dataManager).
        Optional<Curso> cursoAprovado = dataManager.getCursos().stream()
                .filter(c -> c.getId().equals(cursoId))
                .findFirst();
        assertTrue(cursoAprovado.isPresent(), "O curso aprovado deve ser encontrado na persistência.");

        // 3. Verificar se o status do curso mudou para ATIVO.
        assertEquals(StatusCurso.ATIVO, cursoAprovado.get().getStatus(), "O status do curso deve mudar para ATIVO.");
    }

    @Test
    void rejeitarCursoDeveMudarStatusParaInativo() {
        // TODO: Testar a rejeição de um curso (ex: "c1"):
        String cursoId = "c1";
        
        // 1. Chamar rejeitarCurso() e verificar se o retorno é 'true'.
        boolean resultado = cursoService.rejeitarCurso(cursoId);
        assertTrue(resultado, "O curso foi rejeitado.");

        // 2. Recuperar o curso na persistência (dataManager).
        Optional<Curso> cursoRejeitado = dataManager.getCursos().stream()
                .filter(c -> c.getId().equals(cursoId))
                .findFirst();
        assertTrue(cursoRejeitado.isPresent(), "O curso rejeitado deve ser encontrado na persistência.");

        // 3. Verificar se o status do curso mudou para INATIVO.
        assertEquals(StatusCurso.INATIVO, cursoRejeitado.get().getStatus(), "O status do curso deve mudar para INATIVO.");
    }

    // REQUISITO: Visualizar catálogo de cursos disponíveis (Estudante/Comum)
    @Test
    void visualizarCatalogoDeveRetornarApenasCursosAtivos() {
        // TODO: Testar a visualização do catálogo:
        // 1. Chamar visualizarCatalogo().
        // 2. Verificar se a lista retornada contém APENAS cursos com StatusCurso.ATIVO.
        // 3. Verificar se o tamanho da lista está correto (baseado nos dados iniciais).

    }

    // REQUISITO: Ingressar em cursos (com inserção de PIN quando necessário)
    @Test
    void ingressarCursoComPinDeveFuncionarComPinCorreto() {
        // TODO: Testar o ingresso em um curso com PIN (ex: "c2"):
        // 1. Garantir que o curso está ATIVO (configurar se necessário).
        // 2. Chamar ingressarCurso() com o PIN CORRETO.
        // 3. Verificar se o retorno é 'true'.
    }

    @Test
    void ingressarCursoComPinDeveFalharComPinIncorreto() {
        // TODO: Testar o ingresso em um curso com PIN (ex: "c2"):
        // 1. Garantir que o curso está ATIVO (configurar se necessário).
        // 2. Chamar ingressarCurso() com um PIN INCORRETO.
        // 3. Verificar se o retorno é 'false'.
    }

    @Test
    void ingressarCursoSemPinDeveFuncionar() {
        // TODO: Testar o ingresso em um curso SEM PIN (ex: "c1"):
        // 1. Chamar ingressarCurso() passando 'null' ou uma string vazia como PIN.
        // 2. Verificar se o retorno é 'true'.
    }
}