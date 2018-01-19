package hs.ontheroadstore.message.deliver.handle;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import hs.ontheroadstore.message.deliver.bean.AppPropertyKeyConst;
import org.apache.log4j.Logger;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.FluentJdbcException;
import org.codejargon.fluentjdbc.api.FluentJdbcSqlException;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResult;
import org.codejargon.fluentjdbc.api.query.listen.AfterQueryListener;
import org.codejargon.fluentjdbc.api.query.listen.ExecutionDetails;

/**
 * Created by Jeffrey(zuoyaofei@icloud.com) on 18/1/3.
 */
public class HsWeixinNoDisturbHandleImpl implements NoDisturbHandle {

    Properties prop;
    DataSource dataSource;
    FluentJdbc fluentJdbc;
    private final Logger logger = Logger.getLogger(HsWeixinNoDisturbHandleImpl.class);
    public HsWeixinNoDisturbHandleImpl(final Properties prop) {
        this.prop = prop;
        this.dataSource = this.getDataSource(prop);
    }

    @Override

    public void pacify(String uid) {
        if (fluentJdbc == null) {
            fluentJdbc = this.buildJdbc(this.dataSource);
        }
        if (fluentJdbc == null) {
            logger.error("jdbc build failed.");
        }
        Query query = fluentJdbc.query();
        this.setNoDisturb(query,uid);
    }

    private void setNoDisturb(Query query,String uid) {

        try {
            UpdateResult result = query.update("UPDATE sp_hs_user_device set no_disturbe=1 WHERE wx_web_openid=?")
                    .params(uid)
                    .run();
            if (result.affectedRows()>0L) {
                logger.error(uid + " no disturbed.");
            }
        } catch (FluentJdbcSqlException e) {
            logger.error(e.getMessage());
        } catch (FluentJdbcException e) {
            logger.error(e.getMessage());
            try {
                dataSource.getConnection().close();
                fluentJdbc = null;
            } catch (SQLException e1) {
                logger.error("close db exception:" + e1.getMessage());
            }
        }

    }
    private FluentJdbc buildJdbc(final DataSource ds) {
        if(ds == null) return null;
        try {
            return new FluentJdbcBuilder()
                    .connectionProvider(dataSource)
                    .afterQueryListener(new AfterQueryListener() {
                        @Override
                        public void listen(ExecutionDetails executionDetails) {
                            if (!executionDetails.success()) {
                                logger.error(executionDetails.sql());
                            }
                        }
                    })
                    .build();

        }catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private DataSource getDataSource(final Properties prop) {

        String host = prop.getProperty(AppPropertyKeyConst.DB_HOST_KEY);
        String db_name = prop.getProperty(AppPropertyKeyConst.DB_NAME_KEY);
        if (host == null) {
            return null;
        }
        if (db_name == null || db_name.equals("")) {
            return null;
        }
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://" +
                prop.getProperty(AppPropertyKeyConst.DB_HOST_KEY) + ":3306/" +
                prop.getProperty(AppPropertyKeyConst.DB_NAME_KEY);
        ds.setUrl(url);
        ds.setUsername(prop.getProperty(AppPropertyKeyConst.DB_USER_KEY));
        ds.setPassword(prop.getProperty(AppPropertyKeyConst.DB_PASSWORD_KEY));
        return ds;
    }
}
