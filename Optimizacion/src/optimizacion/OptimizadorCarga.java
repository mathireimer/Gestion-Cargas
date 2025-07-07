package optimizacion;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

    public static int calcularDistanciaRuta(ArrayList<String> ruta) {
        HashMap<String, Integer> distancias = new HashMap<>();
        distancias.put("Asunción", 0);
        distancias.put("Lambaré", 12);
        distancias.put("Ciudad del Este", 327);
        distancias.put("Encarnación", 375);
        distancias.put("Canindeyu", 437);

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
