package br.com.fiap.cheffy.shared.constants;

public enum FlowConstants {
    TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW("CreateFoodItemUseCase"),
    TRIAGE_FIND_FOOD_ITEM_BY_ID_FLOW("FindFoodItemByIdUseCase"),
    TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW("UpdateFoodItemUseCase");

    public  final String name;
    FlowConstants(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
