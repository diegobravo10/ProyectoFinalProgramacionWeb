package ec.edu.ups.proyecto.services;

import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Path("/reportes")
public class ReporteResource {

    @Resource(lookup = "java:/CitaDS")
    private DataSource dataSource;

    @GET
    @Path("/doctor")
    @Produces("application/pdf")
    public Response reporteDoctor(@QueryParam("fechaInicio") String fechaInicio,
                                  @QueryParam("fechaFin") String fechaFin,
                                  @QueryParam("doctorId") int doctorId) {
        Map<String, Object> params = new HashMap<>();
        params.put("FECHA_INICIO", java.sql.Date.valueOf(fechaInicio));
        params.put("FECHA_FIN", java.sql.Date.valueOf(fechaFin));
        params.put("DOCTOR_ID", doctorId);  // parámetro extra para Jasper

        return generarPDF(
            "C:/Users/user/JaspersoftWorkspace/MyReports/Invoice.jasper",
            params, 
            "reporteDoctor.pdf"
        );
    }

    @GET
    @Path("/especialidad")
    @Produces("application/pdf")
    public Response reporteEspecialidad(@QueryParam("fechaInicio") String fechaInicio,
                                        @QueryParam("fechaFin") String fechaFin,
                                        @QueryParam("especialidad") String especialidad) {
        Map<String, Object> params = new HashMap<>();
        params.put("FECHA_INICIO", java.sql.Date.valueOf(fechaInicio));
        params.put("FECHA_FIN", java.sql.Date.valueOf(fechaFin));
        params.put("ESPECIALIDAD", especialidad);  // parámetro extra para Jasper

        return generarPDF(
            "C:/Users/user/JaspersoftWorkspace/MyReports/reportedoc.jasper",
            params, 
            "reporteEspecialidad.pdf"
        );
    }

    @GET
    @Path("/general")
    @Produces("application/pdf")
    public Response reporteGeneral(@QueryParam("fechaInicio") String fechaInicio,
                                   @QueryParam("fechaFin") String fechaFin) {
        Map<String, Object> params = new HashMap<>();
        params.put("FECHA_INICIO", java.sql.Date.valueOf(fechaInicio));
        params.put("FECHA_FIN", java.sql.Date.valueOf(fechaFin));

        return generarPDF(
            "C:/Users/user/JaspersoftWorkspace/MyReports/allEspecialidades.jasper",
            params, 
            "reporteGeneral.pdf"
        );
    }

    private Response generarPDF(String rutaReporte, Map<String, Object> params, String nombreArchivo) {
        try (Connection conn = dataSource.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(rutaReporte, params, conn);
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            return Response.ok(pdf)
                    .header("Content-Disposition", "attachment; filename=" + nombreArchivo)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
