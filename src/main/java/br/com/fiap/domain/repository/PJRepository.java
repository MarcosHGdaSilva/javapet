package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.pessoa.PJ;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PJRepository implements Repository<PJ, Long>{
    private ConnectionFactory factory;

    private static final AtomicReference<PJRepository> instance = new AtomicReference<>();

    private PJRepository() {
        this.factory = ConnectionFactory.build();
    }

    public static PJRepository build() {
        instance.compareAndSet(null, new PJRepository());
        return instance.get();
    }

    @Override
    public List<PJ> findAll() {
        List<PJ> list = new ArrayList<>();
        Connection con = factory.getConnection();
        ResultSet rs = null;
        Statement st = null;
        try {
            String sql = "SELECT * FROM TB_PF";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Long id = rs.getLong("ID_JURIDICA");
                    String nome = rs.getString("NM_JURIDICA");
                    LocalDate nascimento = rs.getDate("DT_FUNDACAO").toLocalDate();
                    String tipo = rs.getString("TP_PESSOA");
                    String CNPJ = rs.getString("NR_CNPJ");
                    list.add(new PJ(id, nome, nascimento, CNPJ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, st, con);
        }
        return list;
    }


    @Override
    public PJ findById(Long id) {
        PJ pessoa = null;
        var sql = "SELECT * FROM TB_PJ where ID_JURIDICA = ?";
        Connection con = factory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nome = rs.getString("NM_JURIDICA");
                    LocalDate nascimento = rs.getDate("DT_FUNDACAO").toLocalDate();
                    String CNPJ = rs.getString("NR_CNPJ");
                    pessoa = new PJ(id, nome, nascimento, CNPJ);
                }
            } else {
                System.out.println("Dados não encontrados com o id: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível consultar os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(rs, ps, con);
        }
        return pessoa;
    }


    @Override
    public PJ persiste(PJ pj) {

        var sql = "BEGIN INSERT INTO TB_PJ (NM_JURIDICA , DT_FUNDACAO, TP_PESSOA, NR_CNPJ) VALUES (?,?,?,?) returning ID_JURIDICA into ?; END;";

        Connection con = factory.getConnection();
        CallableStatement cs = null;

        try {

            cs = con.prepareCall(sql);
            cs.setString(1, pj.getNome());
            cs.setDate(2, Date.valueOf(pj.getNascimento()));
            cs.setString(3, pj.getTipo());
            cs.setString(4, pj.getCNPJ());
            cs.registerOutParameter(5, Types.BIGINT);
            cs.executeUpdate();
            pj.setId(cs.getLong(5));

        } catch (SQLException e) {
            System.err.println("Não foi possível inserir os dados!\n" + e.getMessage());
        } finally {
            fecharObjetos(null, cs, con);
        }
        return pj;
    }
    private static void fecharObjetos(ResultSet rs, Statement st, Connection con) {
        try {
            if (Objects.nonNull(rs) && !rs.isClosed()) {
                rs.close();
            }
            st.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Erro ao encerrar o ResultSet, a Connection e o Statment!\n" + e.getMessage());
        }
    }
}
