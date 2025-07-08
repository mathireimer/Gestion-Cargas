package optimizacion;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.List;

public class OptimizadorCarga {

    public static ArrayList<Mercaderia> cargarMercaderias(String archivo) {
        ArrayList<Mercaderia> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0].split(":")[1].trim());
                double peso = Double.parseDouble(partes[1].split(":")[1].trim());
                double volumen = Double.parseDouble(partes[2].split(":")[1].trim());
                String destino = partes[3].split(":")[1].trim();
                int distancia = Integer.parseInt(partes[4].split(":")[1].trim());
                lista.add(new Mercaderia(id, peso, volumen, destino, distancia));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ArrayList<Remolque> cargarRemolques(String archivo) {
        ArrayList<Remolque> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0].split(":")[1].trim());
                String nombre = partes[1].split(":")[1].trim();
                double volumen = Double.parseDouble(partes[2].split(":")[1].trim());
                double peso = Double.parseDouble(partes[3].split(":")[1].trim());
                lista.add(new Remolque(id, nombre, peso, volumen, "Almacén", "", 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ArrayList<Asignacion> optimizarAsignacion(ArrayList<Remolque> remolques, ArrayList<Mercaderia> mercaderias) {
        ArrayList<Asignacion> asignaciones = new ArrayList<>();

        // Ordenar mercaderías por distancia (mayor a menor)
        mercaderias.sort((m1, m2) -> Integer.compare(m2.getDistancia(), m1.getDistancia()));

        // Ordenar remolques por volumen y peso (menor a mayor)
        remolques.sort((r1, r2) -> {
            int cmp = Double.compare(r1.getCapacidadVolumen(), r2.getCapacidadVolumen());
            if (cmp == 0) {
                return Double.compare(r1.getCapacidadPeso(), r2.getCapacidadPeso());
            }
            return cmp;
        });

        for (Mercaderia merc : mercaderias) {
            for (Remolque rem : remolques) {
                if (rem.puedeCargar(merc)) {
                    rem.asignarCarga(merc);
                    asignaciones.add(new Asignacion(merc, rem));
                    break;
                }
            }
        }

        // Agrupar mercaderías por remolque
        Map<Integer, ArrayList<Mercaderia>> asignacionesPorRemolque = new HashMap<>();
        for (Asignacion asignacion : asignaciones) {
            int remolqueId = asignacion.getRemolque().getId();
            asignacionesPorRemolque.putIfAbsent(remolqueId, new ArrayList<>());
            asignacionesPorRemolque.get(remolqueId).add(asignacion.getMercaderia());
        }

// Calcular y mostrar costos
        for (Map.Entry<Integer, ArrayList<Mercaderia>> entry : asignacionesPorRemolque.entrySet()) {
            ArrayList<Mercaderia> mercaderiasRemolque = entry.getValue();

            ArrayList<String> destinos = new ArrayList<>();
            double pesoTotal = 0;
            for (Mercaderia m : mercaderiasRemolque) {
                pesoTotal += m.getPeso();
                if (!destinos.contains(m.getDestino())) {
                    destinos.add(m.getDestino());
                }
            }

            int distanciaTotal = calcularDistanciaRuta(destinos);
            double consumoTotal = distanciaTotal * (0.3 + pesoTotal * 0.0001);
            double costoTotal = consumoTotal * 1.25;

            System.out.println("Remolque #" + entry.getKey());
            System.out.println("Mercaderías transportadas: ");
            for (Mercaderia m : mercaderiasRemolque) {
                System.out.println("  - ID: " + m.getId() + ", Destino: " + m.getDestino() + ", Peso: " + m.getPeso());
            }
            System.out.println("Destinos visitados: " + destinos);
            System.out.println("Peso Total: " + pesoTotal + " kg");
            System.out.println("Distancia Total (ida/vuelta): " + distanciaTotal + " km");
            System.out.println("Costo Total: $" + String.format("%.2f", costoTotal) + " USD\n");
        }

        return asignaciones;
    }

    
     private static double[] calcularCosto(double pesoTotal, List<String> ruta) {
        double consumoBasePorKm = 0.3;
        double incrementoPorKg = 0.0001;
        double costoCombustiblePorLitro = 1.25;

        Map<String, Integer> destinos = new HashMap<>();
        destinos.put("Asunción", 0);
        destinos.put("Lambaré", 12);
        destinos.put("Ciudad del Este", 327);
        destinos.put("Encarnación", 375);
        destinos.put("Canindeyu", 425);

        double distanciaTotal = 0;
        List<String> rutaCompleta = new ArrayList<>();
        rutaCompleta.add("Asunción");
        rutaCompleta.addAll(ruta);
        rutaCompleta.add("Asunción");

        for (int i = 0; i < rutaCompleta.size() - 1; i++) {
            String origen = rutaCompleta.get(i);
            String destino = rutaCompleta.get(i + 1);
            int distancia = 0;
            if (origen.equals("Asunción")) {
                distancia = destinos.get(destino);
            } else if (destino.equals("Asunción")) {
                distancia = destinos.get(origen);
            } else {
                distancia = destinos.get(origen) + destinos.get(destino);
            }
            distanciaTotal += distancia;
        }

        double consumoTotal = distanciaTotal * (consumoBasePorKm + pesoTotal * incrementoPorKg);
        double costoTotal = consumoTotal * costoCombustiblePorLitro;

        return new double[]{distanciaTotal, costoTotal};
    }

    public static List<Asignacion> optimizar(List<Mercaderia> mercaderias, List<Remolque> remolques) {
        List<Asignacion> mejoresAsignaciones = new ArrayList<>();
        Set<String> asignacionesUnicas = new HashSet<>();
        double mejorCostoTotal = Double.MAX_VALUE;

        int n = mercaderias.size();
        int totalCombinaciones = 1 << n; // 2^n

        for (int i = 1; i < totalCombinaciones; i++) {
            List<Mercaderia> grupo1 = new ArrayList<>();
            List<Mercaderia> grupo2 = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (((i >> j) & 1) == 1) {
                    grupo1.add(mercaderias.get(j));
                } else {
                    grupo2.add(mercaderias.get(j));
                }
            }

            List<List<Mercaderia>> combinacion1 = Arrays.asList(grupo1, grupo2);
            List<List<Mercaderia>> combinacion2 = Arrays.asList(grupo2, grupo1);

            for (List<List<Mercaderia>> combinacion : Arrays.asList(combinacion1, combinacion2)) {
                if (combinacion.size() > remolques.size()) continue;

                List<Asignacion> asignacionesActuales = new ArrayList<>();
                double costoTotalCombinacion = 0;
                boolean esValido = true;
                StringBuilder hashAsignacion = new StringBuilder();

                for (int idx = 0; idx < combinacion.size(); idx++) {
                    List<Mercaderia> carga = combinacion.get(idx);
                    Remolque remolque = remolques.get(idx);

                    double pesoTotal = carga.stream().mapToDouble(Mercaderia::getPeso).sum();
                    double volumenTotal = carga.stream().mapToDouble(Mercaderia::getVolumen).sum();

                    if (pesoTotal > remolque.getPesoMaximo() || volumenTotal > remolque.getVolumenMaximo()) {
                        esValido = false;
                        break;
                    }

                    List<String> destinos = carga.stream()
                            .map(Mercaderia::getDestino)
                            .distinct()
                            .collect(Collectors.toList());

                    double[] distanciaCosto = calcularCosto(pesoTotal, destinos);
                    double distancia = distanciaCosto[0];
                    double costo = distanciaCosto[1];

                    hashAsignacion.append(remolque.getId()).append("-").append(carga.toString()).append("-");

                    Asignacion asignacion = new Asignacion (remolque);
                    carga.forEach(asignacion::agregarMercaderia);
                    asignacionesActuales.add(asignacion);
                    costoTotalCombinacion += costo;
                }

                if (esValido && !asignacionesUnicas.contains(hashAsignacion.toString())) {
                    asignacionesUnicas.add(hashAsignacion.toString());
                    if (costoTotalCombinacion < mejorCostoTotal) {
                        mejorCostoTotal = costoTotalCombinacion;
                        mejoresAsignaciones = asignacionesActuales;
                    }
                }
            }
        }
        return mejoresAsignaciones;
    }
    
    public static int calcularDistanciaRuta(ArrayList<String> ruta) {
        HashMap<String, Integer> distancias = new HashMap<>();
        distancias.put("Asunción", 0);
        distancias.put("Lambaré", 12);
        distancias.put("Ciudad del Este", 327);
        distancias.put("Encarnación", 375);
        distancias.put("Canindeyu", 425);

        int distanciaTotal = 0;
        ArrayList<String> rutaCompleta = new ArrayList<>();
        rutaCompleta.add("Asunción");
        rutaCompleta.addAll(ruta);
        rutaCompleta.add("Asunción");

        for (int i = 0; i < rutaCompleta.size() - 1; i++) {
            String desde = rutaCompleta.get(i);
            String hacia = rutaCompleta.get(i + 1);
            int distancia;
            if (desde.equals("Asunción") || hacia.equals("Asunción")) {
                distancia = distancias.get(desde.equals("Asunción") ? hacia : desde);
            } else {
                distancia = distancias.get(desde) + distancias.get(hacia);
            }
            distanciaTotal += distancia;
        }

        return distanciaTotal;
    }
}
