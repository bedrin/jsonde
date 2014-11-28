package com.jsonde.api.function.heap;

import com.jsonde.api.function.FunctionRequest;
import com.jsonde.api.function.FunctionResponse;

import java.util.HashSet;
import java.util.Set;

public class DumpHeapFunctionResponse extends FunctionResponse<DumpHeapFunctionRequest> {

    private Set<ClassHeapDataDto> classHeapDataDtos;

    public DumpHeapFunctionResponse(FunctionRequest request) {
        super(request);
        classHeapDataDtos = new HashSet<ClassHeapDataDto>();
    }

    public Set<ClassHeapDataDto> getClassHeapDataDtos() {
        return classHeapDataDtos;
    }

    public void setClassHeapDataDtos(Set<ClassHeapDataDto> classHeapDataDtos) {
        this.classHeapDataDtos = classHeapDataDtos;
    }

    public void addClassHeapDataDto(ClassHeapDataDto classHeapDataDto) {
        classHeapDataDtos.add(classHeapDataDto);
    }

}
