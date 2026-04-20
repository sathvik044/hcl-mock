package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ItineraryRequestDTO;
import com.example.demo.dto.ItineraryResponseDTO;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.TravelRequest;
import com.example.demo.enums.ItineraryStatus;
import com.example.demo.mapper.ItineraryMapper;
import com.example.demo.repository.ItineraryRepository;
import com.example.demo.repository.TravelRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelRequestRepository travelRequestRepository;

    
    public ItineraryResponseDTO create(ItineraryRequestDTO dto) {

        TravelRequest travelRequest = travelRequestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new RuntimeException("Travel Request not found"));

        Itinerary itinerary = ItineraryMapper.toEntity(dto, travelRequest);

        Itinerary saved = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(saved);
    }

    public List<ItineraryResponseDTO> getByTravelRequest(Long requestId) {

        List<Itinerary> itineraries = itineraryRepository.findByTravelRequestId(requestId);

        return itineraries.stream()
                .map(ItineraryMapper::toDTO)
                .collect(Collectors.toList());
    }
    public ItineraryResponseDTO confirm(Long id) {

        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (itinerary.getStatus() != ItineraryStatus.PENDING) {
            throw new RuntimeException("Only pending itineraries can be confirmed");
        }

        itinerary.setStatus(ItineraryStatus.CONFIRMED);

        Itinerary updated = itineraryRepository.save(itinerary);

        return ItineraryMapper.toDTO(updated);
    }
}