package org.example.tpoprogramacioniii.service.impl;

import org.example.tpoprogramacioniii.dto.request.QuickSortRequestDTO;
import org.example.tpoprogramacioniii.dto.response.QuickSortResponseDTO;
import org.example.tpoprogramacioniii.Enum.AlgorithmEnum;
import org.example.tpoprogramacioniii.service.QuickSortServiceI;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuickSortServiceImpl implements QuickSortServiceI {
    
    private int lastComparisons = 0;
    private int lastSwaps = 0;
    private long lastExecutionTime = 0;
    
    @Override
    public <T extends Comparable<T>> QuickSortResponseDTO<T> sortElements(QuickSortRequestDTO<T> request) {
        long startTime = System.currentTimeMillis();
        lastComparisons = 0;
        lastSwaps = 0;
        
        try {
            List<T> elements = new ArrayList<>(request.getElementsToSort());
            boolean ascending = request.getAscendingOrder() != null ? request.getAscendingOrder() : true;
            
            quickSort(elements, 0, elements.size() - 1, ascending);
            
            if (request.getLimit() != null && request.getLimit() > 0 && request.getLimit() < elements.size()) {
                elements = elements.subList(0, request.getLimit());
            }
            
            lastExecutionTime = System.currentTimeMillis() - startTime;
            
            return new QuickSortResponseDTO<>(
                elements, AlgorithmEnum.QUICK_SORT, lastExecutionTime,
                request.getElementsToSort().size(), lastComparisons, lastSwaps,
                request.getSortCriteria() != null ? request.getSortCriteria().name() : "DEFAULT", ascending);
            
        } catch (Exception e) {
            lastExecutionTime = System.currentTimeMillis() - startTime;
            return new QuickSortResponseDTO<>(new ArrayList<>(), AlgorithmEnum.QUICK_SORT, 
                lastExecutionTime, 0, lastComparisons, lastSwaps, "ERROR", true);
        }
    }
    
    @Override
    public QuickSortResponseDTO<String> sortLocationsByDistance(List<String> locationIds, 
                                                               double referenceLatitude, 
                                                               double referenceLongitude, 
                                                               boolean ascendingOrder) {
        List<LocationDistance> locationDistances = new ArrayList<>();
        for (String locationId : locationIds) {
            double lat = Math.random() * 180 - 90;
            double lon = Math.random() * 360 - 180;
            double distance = calculateDistance(referenceLatitude, referenceLongitude, lat, lon);
            locationDistances.add(new LocationDistance(locationId, distance));
        }
        
        quickSort(locationDistances, 0, locationDistances.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (LocationDistance ld : locationDistances) {
            sortedIds.add(ld.locationId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, locationIds.size(), lastComparisons, lastSwaps, "DISTANCE", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortTasksByPriority(List<String> taskIds, boolean ascendingOrder) {
        List<TaskPriority> taskPriorities = new ArrayList<>();
        for (String taskId : taskIds) {
            int priority = (int) (Math.random() * 10) + 1;
            taskPriorities.add(new TaskPriority(taskId, priority));
        }
        
        quickSort(taskPriorities, 0, taskPriorities.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (TaskPriority tp : taskPriorities) {
            sortedIds.add(tp.taskId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, taskIds.size(), lastComparisons, lastSwaps, "PRIORITY", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortTasksByCreationTime(List<String> taskIds, boolean ascendingOrder) {
        List<TaskTime> taskTimes = new ArrayList<>();
        for (String taskId : taskIds) {
            long creationTime = System.currentTimeMillis() - (long) (Math.random() * 86400000);
            taskTimes.add(new TaskTime(taskId, creationTime));
        }
        
        quickSort(taskTimes, 0, taskTimes.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (TaskTime tt : taskTimes) {
            sortedIds.add(tt.taskId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, taskIds.size(), lastComparisons, lastSwaps, "CREATION_TIME", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortVehiclesByCapacity(List<String> vehicleIds, boolean ascendingOrder) {
        List<VehicleCapacity> vehicleCapacities = new ArrayList<>();
        for (String vehicleId : vehicleIds) {
            int capacity = (int) (Math.random() * 5000) + 500;
            vehicleCapacities.add(new VehicleCapacity(vehicleId, capacity));
        }
        
        quickSort(vehicleCapacities, 0, vehicleCapacities.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (VehicleCapacity vc : vehicleCapacities) {
            sortedIds.add(vc.vehicleId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, vehicleIds.size(), lastComparisons, lastSwaps, "CAPACITY", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortVehiclesByFuelConsumption(List<String> vehicleIds, boolean ascendingOrder) {
        List<VehicleConsumption> vehicleConsumptions = new ArrayList<>();
        for (String vehicleId : vehicleIds) {
            double consumption = Math.random() * 15 + 5;
            vehicleConsumptions.add(new VehicleConsumption(vehicleId, consumption));
        }
        
        quickSort(vehicleConsumptions, 0, vehicleConsumptions.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (VehicleConsumption vc : vehicleConsumptions) {
            sortedIds.add(vc.vehicleId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, vehicleIds.size(), lastComparisons, lastSwaps, "FUEL_CONSUMPTION", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortSegmentsByDistance(List<String> segmentIds, boolean ascendingOrder) {
        List<SegmentDistance> segmentDistances = new ArrayList<>();
        for (String segmentId : segmentIds) {
            double distance = Math.random() * 100;
            segmentDistances.add(new SegmentDistance(segmentId, distance));
        }
        
        quickSort(segmentDistances, 0, segmentDistances.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (SegmentDistance sd : segmentDistances) {
            sortedIds.add(sd.segmentId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, segmentIds.size(), lastComparisons, lastSwaps, "DISTANCE", ascendingOrder);
    }
    
    @Override
    public QuickSortResponseDTO<String> sortSegmentsByTravelTime(List<String> segmentIds, boolean ascendingOrder) {
        List<SegmentTime> segmentTimes = new ArrayList<>();
        for (String segmentId : segmentIds) {
            double time = Math.random() * 60;
            segmentTimes.add(new SegmentTime(segmentId, time));
        }
        
        quickSort(segmentTimes, 0, segmentTimes.size() - 1, ascendingOrder);
        
        List<String> sortedIds = new ArrayList<>();
        for (SegmentTime st : segmentTimes) {
            sortedIds.add(st.segmentId);
        }
        
        return new QuickSortResponseDTO<>(sortedIds, AlgorithmEnum.QUICK_SORT, 
            lastExecutionTime, segmentIds.size(), lastComparisons, lastSwaps, "TRAVEL_TIME", ascendingOrder);
    }
    
    @Override
    public Map<String, Object> getLastSortingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("comparisons", lastComparisons);
        stats.put("swaps", lastSwaps);
        stats.put("executionTimeMs", lastExecutionTime);
        stats.put("algorithm", "QUICK_SORT");
        return stats;
    }
    
    @Override
    public <T extends Comparable<T>> boolean isSorted(List<T> elements, boolean ascendingOrder) {
        if (elements.size() <= 1) return true;
        
        for (int i = 0; i < elements.size() - 1; i++) {
            int comparison = elements.get(i).compareTo(elements.get(i + 1));
            if (ascendingOrder && comparison > 0) return false;
            else if (!ascendingOrder && comparison < 0) return false;
        }
        return true;
    }
    
    // Algoritmo QuickSort
    private <T extends Comparable<T>> void quickSort(List<T> arr, int low, int high, boolean ascending) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, ascending);
            quickSort(arr, low, pivotIndex - 1, ascending);
            quickSort(arr, pivotIndex + 1, high, ascending);
        }
    }
    
    private <T extends Comparable<T>> int partition(List<T> arr, int low, int high, boolean ascending) {
        T pivot = arr.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            lastComparisons++;
            int comparison = arr.get(j).compareTo(pivot);
            boolean shouldSwap = ascending ? (comparison <= 0) : (comparison >= 0);
            
            if (shouldSwap) {
                i++;
                swap(arr, i, j);
            }
        }
        
        swap(arr, i + 1, high);
        return i + 1;
    }
    
    private <T> void swap(List<T> arr, int i, int j) {
        if (i != j) {
            T temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
            lastSwaps++;
        }
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    // Clases auxiliares
    private static class LocationDistance implements Comparable<LocationDistance> {
        String locationId; double distance;
        LocationDistance(String locationId, double distance) { this.locationId = locationId; this.distance = distance; }
        @Override public int compareTo(LocationDistance other) { return Double.compare(this.distance, other.distance); }
    }
    
    private static class TaskPriority implements Comparable<TaskPriority> {
        String taskId; int priority;
        TaskPriority(String taskId, int priority) { this.taskId = taskId; this.priority = priority; }
        @Override public int compareTo(TaskPriority other) { return Integer.compare(this.priority, other.priority); }
    }
    
    private static class TaskTime implements Comparable<TaskTime> {
        String taskId; long creationTime;
        TaskTime(String taskId, long creationTime) { this.taskId = taskId; this.creationTime = creationTime; }
        @Override public int compareTo(TaskTime other) { return Long.compare(this.creationTime, other.creationTime); }
    }
    
    private static class VehicleCapacity implements Comparable<VehicleCapacity> {
        String vehicleId; int capacity;
        VehicleCapacity(String vehicleId, int capacity) { this.vehicleId = vehicleId; this.capacity = capacity; }
        @Override public int compareTo(VehicleCapacity other) { return Integer.compare(this.capacity, other.capacity); }
    }
    
    private static class VehicleConsumption implements Comparable<VehicleConsumption> {
        String vehicleId; double consumption;
        VehicleConsumption(String vehicleId, double consumption) { this.vehicleId = vehicleId; this.consumption = consumption; }
        @Override public int compareTo(VehicleConsumption other) { return Double.compare(this.consumption, other.consumption); }
    }
    
    private static class SegmentDistance implements Comparable<SegmentDistance> {
        String segmentId; double distance;
        SegmentDistance(String segmentId, double distance) { this.segmentId = segmentId; this.distance = distance; }
        @Override public int compareTo(SegmentDistance other) { return Double.compare(this.distance, other.distance); }
    }
    
    private static class SegmentTime implements Comparable<SegmentTime> {
        String segmentId; double time;
        SegmentTime(String segmentId, double time) { this.segmentId = segmentId; this.time = time; }
        @Override public int compareTo(SegmentTime other) { return Double.compare(this.time, other.time); }
    }
}
