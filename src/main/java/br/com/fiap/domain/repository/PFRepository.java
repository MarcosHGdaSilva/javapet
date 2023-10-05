package br.com.fiap.domain.repository;
import br.com.fiap.domain.entity.pessoa.PF;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PFRepository implements Repository<PF, Long>{


    private static final AtomicReference<PFRepository> instance = new AtomicReference<>();

    private ConnectionFactory factory;

    private   PFRepository(){
        this.factory = ConnectionFactory.build();
    }

    public static PFRepository build(){
        instance.compareAndSet(null, new PFRepository());
        return instance.get();
    }


    @Override
    public List<PF> findAll() {
        List<PF> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            st = con.createStatement();
            String sql = "SELECT * FROM TB_PF";
            rs = st.executeQuery(sql);
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    Long id = rs.getLong("ID_PESSOA");
                    String nome = rs.getString("NM_PESSOA");
                    LocalDate nascimento = rs.getDate("DT_NASCIMENTO").toLocalDate();
                    String tipo = rs.getString("TP_PESSOA");
                    String cpf = rs.getString("NR_CPF");
                    list.add(new PF(id, nome, nascimento, cpf));
                }
            }

        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, st, con);
        }
        return list;
    }

    private static void fecharObjetos(ResultSet rs, Statement st, Connection con) {
        try {
            boolean closed = rs.isClosed();
            if (Objects.nonNull(rs) && !closed) {
                rs.close();
            }
            st.close();
            con.close();
        }catch (SQLException e) {
            System.err.println("Erro ao encerrar o ResultSet, a Connection e o Statement!\n" + e.getMessage());
        }
    }

    @Override
    public PF findById(Long id) {
        return null;
    }

    @Override
    public List<PF> findByTexto(String texto) {
        return null;
    }

    @Override
    public PF persiste(PF pf) {
        return null;
    }

    @Override
    public PF update(PF pf) {
        return null;
    }

    @Override
    public boolean delete(PF pf) {
        return false;
    }
}
