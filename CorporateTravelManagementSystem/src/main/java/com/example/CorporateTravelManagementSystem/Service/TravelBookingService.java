package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TravelBookingRequestDTO;
import com.example.demo.dto.TravelBookingResponseDTO;
import com.example.demo.entity.Itinerary;
import com.example.demo.entity.TravelBooking;
import com.example.demo.entity.TravelRequest;
import com.example.demo.entity.User;
import com.example.demo.mapper.TravelBookingMapper;
import com.example.demo.repository.ItineraryRepository;
import com.example.demo.repository.TravelBookingRepository;
import com.example.demo.repository.TravelRequestRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelBookingService {

    private final TravelBookingRepository bookingRepository;
    private final TravelRequestRepository travelRequestRepository;
    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;

   
    public TravelBookingResponseDTO create(TravelBookingRequestDTO dto) {

        TravelRequest request = travelRequestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new RuntimeException("Travel request not found"));

        Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        User user = userRepository.findById(dto.getBookedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!request.getStatus().toString().equals("FINANCE_APPROVED")) {
            throw new RuntimeException("Booking allowed only after finance approval");
        }

        TravelBooking booking = TravelBookingMapper.toEntity(dto, request, itinerary, user);

        return TravelBookingMapper.toDTO(bookingRepository.save(booking));
    }


    public List<TravelBookingResponseDTO> getByRequest(Long requestId) {

        return bookingRepository.findByTravelRequestId(requestId)
                .stream()
                .map(TravelBookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}